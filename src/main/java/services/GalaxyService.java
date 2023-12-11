package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Galaxy {
    private final List<List<Character>> input;
    private final int inputRowSize;
    private final int inputColumnSize;

    private final List<Integer> rowsHasNoGalaxies;
    private final List<Integer> colsHasNoGalaxies;

    private final List<List<Integer>> galaxyCoordinates;

    Galaxy(String fileName) {
        input = FileService.readStringFromDocument(fileName)
                .lines()
                .map(line -> Arrays.stream(line.split(""))
                        .map(value -> value.charAt(0))
                        .toList())
                .collect(Collectors.toCollection(ArrayList::new));

        inputRowSize = input.size();
        inputColumnSize = input.get(0).size();

        rowsHasNoGalaxies = IntStream.range(0, inputRowSize)
                .filter(index -> hasNoGalaxies(true, index)).boxed()
                .toList();

        colsHasNoGalaxies = IntStream.range(0, inputColumnSize)
                .filter(index -> hasNoGalaxies(false, index)).boxed()
                .toList();

        galaxyCoordinates = new ArrayList<>();
        getGalaxyCoordinates();
    }

    private boolean hasNoGalaxies(boolean isRow, int index) {
        if (isRow) {
            for (var rowValue: input.get(index)) {
                if (rowValue.compareTo('#') == 0) {
                    return false;
                }
            }
        } else {
            for (var i = 0; i < inputRowSize; i++) {
                if (input.get(i).get(index).compareTo('#') == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private void getGalaxyCoordinates() {
        for (var i = 0; i < inputRowSize; i++) {
            for (var j = 0; j < inputColumnSize; j++) {
                if (input.get(i).get(j).compareTo('#') == 0) {
                    galaxyCoordinates.add(
                            Arrays.asList(i, j)
                    );
                }
            }
        }
    }

    @Override
    public String toString() {
        return "%s %s".formatted(inputRowSize, inputColumnSize);
    }

    int getShortestPathAmong(final int id1, final int id2, final int emptyMultiplier) {
        var rowMin = Math.min(galaxyCoordinates.get(id1).get(0), galaxyCoordinates.get(id2).get(0));
        var rowMax = Math.max(galaxyCoordinates.get(id1).get(0), galaxyCoordinates.get(id2).get(0));

        var colMin = Math.min(galaxyCoordinates.get(id1).get(1), galaxyCoordinates.get(id2).get(1));
        var colMax = Math.max(galaxyCoordinates.get(id1).get(1), galaxyCoordinates.get(id2).get(1));

        var rowLength = rowMax - rowMin +
                (emptyMultiplier == 1 ? 1 : (emptyMultiplier - 1)) * IntStream.range(rowMin, rowMax + 1)
                        .filter(rowsHasNoGalaxies::contains)
                        .count();

        var colLength = colMax - colMin +
                (emptyMultiplier == 1 ? 1 : (emptyMultiplier - 1)) * IntStream.range(colMin, colMax + 1)
                        .filter(colsHasNoGalaxies::contains)
                        .count();

        return (int) (rowLength + colLength);
    }

    long getSumOfLengths(final int emptyMultiplier) {
        long sum = 0;
        for (var i = 0; i < galaxyCoordinates.size() - 1; i++) {
            for (var j = i + 1; j < galaxyCoordinates.size(); j++) {
                sum += getShortestPathAmong(i, j, emptyMultiplier);
            }
        }
        return sum;
    }
}

public class GalaxyService {
    public static long getSumOfLengths(String fileName, int emptyMultiplier) {
        var galaxy = new Galaxy(fileName);
        return galaxy.getSumOfLengths(emptyMultiplier);
    }
}
