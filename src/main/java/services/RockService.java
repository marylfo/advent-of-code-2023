package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class RockMap {
    public static final String MOVABLE_ROCK = ".O";
    public static final String MOVED_ROCK = "O.";
    public static final char ROCK = 'O';
    private List<String> rows;
    private List<String> columns;
    private int rowSize;
    private int colSize;

    public RockMap(String fileName) {
        String input = FileService.readStringFromDocument(fileName);
        rows = input.lines().toList();
        rowSize = rows.size();
        colSize = rows.get(0).length();

        String patternString = Arrays.stream(input.split("\n"))
                .reduce("", (a, b) -> a + b);

        columns = IntStream.range(0, colSize)
                .mapToObj(colIndex -> IntStream.range(0, rowSize)
                        .mapToObj(value -> String.valueOf(
                                patternString.charAt((value * colSize) + colIndex)))
                        .collect(Collectors.joining(""))
                )
                .toList();
    }

    static String slideRocksToLeft(String line) {
        String newLine = line;
        while (newLine.contains(MOVABLE_ROCK)) {
            var location = newLine.indexOf(MOVABLE_ROCK);
            newLine = newLine.substring(0, location) + MOVED_ROCK + newLine.substring(location + 2);
        }

        return newLine;
    }

    static int getLoad(String column) {
        int sum = 0;
        for (int i = 0; i < column.length(); i++) {
            if (column.charAt(i) == ROCK) {
                sum += (column.length() - i);
            }
        }

        return sum;
    }

    void updateRows() {
        String patternString = columns.stream()
                .reduce("", (a, b) -> a + b);

        rows = IntStream.range(0, rowSize)
                .mapToObj(rowIndex -> IntStream.range(0, colSize)
                        .mapToObj(value -> String.valueOf(
                                patternString.charAt((value * rowSize) + rowIndex)))
                        .collect(Collectors.joining(""))
                )
                .toList();
    }

    void updateColumns() {
        String patternString = rows.stream()
                .reduce("", (a, b) -> a + b);

        columns = IntStream.range(0, colSize)
                .mapToObj(colIndex -> IntStream.range(0, rowSize)
                        .mapToObj(value -> String.valueOf(
                                patternString.charAt((value * colSize) + colIndex)))
                        .collect(Collectors.joining(""))
                )
                .toList();
    }

    void spinOneCycle() {
        // get col slide to left N
        columns = columns.stream()
                .map(RockMap::slideRocksToLeft)
                .toList();

        // from col -> new row
        updateRows();

        // get new row and slide to left W
        rows = rows.stream()
                .map(RockMap::slideRocksToLeft)
                .toList();

        // from row -> new col
        updateColumns();

        // get col and slide to right S
        columns = columns.stream()
                .map(col -> new StringBuilder(col).reverse().toString())
                .map(RockMap::slideRocksToLeft)
                .map(col -> new StringBuilder(col).reverse().toString())
                .toList();

        // from col -> new row
        updateRows();

        // get row and slide to right E
        rows = rows.stream()
                .map(row -> new StringBuilder(row).reverse().toString())
                .map(RockMap::slideRocksToLeft)
                .map(row -> new StringBuilder(row).reverse().toString())
                .toList();

        // from row -> col
        updateColumns();
    }

    int getTotalLoadFromSlidingToNorth() {
        return columns.stream()
                .map(RockMap::slideRocksToLeft)
                .map(RockMap::getLoad)
                .reduce(0, Integer::sum);
    }

    int getTotalLoadFromCycle() {
        final int START = 900;
        final int END = 1000;
        final int ONE_BILLION = 1000000000;

        var cycles = new HashMap<Integer, List<Integer>>();

        for (int i = 0; i < END; i++) {
            spinOneCycle();
            var load = columns.stream()
                    .map(RockMap::getLoad)
                    .reduce(0, Integer::sum);

            if (i >= START) {
                if (cycles.containsKey(load)) {
                    cycles.get(load).add(i);
                } else {
                    var list = new ArrayList<Integer>();
                    list.add(i);
                    cycles.put(load, list);
                }
            }
        }

        var loadValueInStart = getLoadValueFromIndex(cycles, START);

        var nextIndex = 1;

        var lengthOfLoop = cycles.get(loadValueInStart).get(nextIndex++) - START;

        // check if this is valid loop
        while ((getLoadValueFromIndex(cycles, START + 1) != getLoadValueFromIndex(cycles, START+lengthOfLoop + 1)) ||
                (getLoadValueFromIndex(cycles, START + 2) != getLoadValueFromIndex(cycles, START+lengthOfLoop + 2))
        ) {
            lengthOfLoop = cycles.get(loadValueInStart).get(nextIndex++) - START;
        }

        var indexInLoop = (ONE_BILLION - START) % lengthOfLoop;

        return getLoadValueFromIndex(cycles, START + indexInLoop - 1);
    }

    private static int getLoadValueFromIndex(HashMap<Integer, List<Integer>> cycles, int index) {
        return cycles.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(index))
                .map(Map.Entry::getKey)
                .toList().get(0);
    }
}

public class RockService {
    public static int getTotalLoad(String fileName) {
        var rockMap = new RockMap(fileName);
        return rockMap.getTotalLoadFromSlidingToNorth();
    }

    public static int getTotalLoadFromCycle(String fileName) {
        var rockMap = new RockMap(fileName);
        return rockMap.getTotalLoadFromCycle();
    }
}
