package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Pattern {
    private List<String> rows;
    private List<String> columns;
    private int rowSize;
    private int colSize;

    Pattern(String pattern) {
        rows = pattern.lines().toList();
        rowSize = rows.size();
        colSize = rows.get(0).length();

        String patternString = Arrays.stream(pattern.split("\n"))
                .reduce("", (a, b) -> a + b);

        columns = IntStream.range(0, colSize)
                .mapToObj(colIndex -> IntStream.range(0, rowSize)
                        .mapToObj(value -> String.valueOf(
                                patternString.charAt((value * colSize) + colIndex)))
                        .collect(Collectors.joining(""))
                       )
                .toList();
    }

    boolean isRowEqual(int row1, int row2) {
        // input row index starts from 1 to match the example counting
        return rows.get(row1-1).compareTo(rows.get(row2-1)) == 0;
    }

    boolean isColumnEqual(int col1, int col2) {
        // input col index starts from 1 to match the example counting
        return columns.get(col1-1).compareTo(columns.get(col2-1)) == 0;
    }

    boolean isRowDifferentWithOneSymbol(int row1, int row2) {
        return IntStream.range(0, colSize)
                .mapToObj(index ->
                        rows.get(row1-1).charAt(index) == rows.get(row2-1).charAt(index))
                .filter(value -> !value)
                .count() == 1;
    }

    boolean isColumnDifferentWithOneSymbol(int col1, int col2) {
        return IntStream.range(0, rowSize)
                .mapToObj(index ->
                        columns.get(col1-1).charAt(index) == columns.get(col2-1).charAt(index))
                .filter(value -> !value)
                .count() == 1;
    }

    int getVerticalAxis() {
        List<Integer> axes = new ArrayList<>();

        for(var i = colSize-1; i > 0 ; i--) {
            if (isColumnEqual(i, i+1)) {
                axes.add(i);
            }
        }

        for (var axis: axes) {
            var isVerticalReflect = true;
            var colCheckSize = Math.min(axis, colSize - axis);

            for(var i = 0; i < colCheckSize; i++) {
                if (!isColumnEqual(axis-i, axis+i+1)) {
                    isVerticalReflect = false;
                }
            }

            if (isVerticalReflect) {
                return axis;
            }
        }

        return -1;
    }

    int getHorizontalAxis() {
        List<Integer> axes = new ArrayList<>();

        for(var i = rowSize-1; i > 0 ; i--) {
            if (isRowEqual(i, i+1)) {
                axes.add(i);
            }
        }

        for (var axis: axes) {
            var isHorizontalReflect = true;
            var rowCheckSize = Math.min(axis, rowSize - axis);

            for (var i = 0; i < rowCheckSize; i++) {
                if (!isRowEqual(axis-i, axis+i+1)) {
                    isHorizontalReflect = false;
                }
            }

            if (isHorizontalReflect) {
                return axis;
            }
        }

        return -1;
    }

    List<Integer> getVerticalAxisWithSmudge() {
        List<Integer> axes = new ArrayList<>();

        for (var i = colSize-1; i > 0 ; i--) {
            if (isColumnDifferentWithOneSymbol(i, i+1) || isColumnEqual(i, i+1)) {
                axes.add(i);
            }
        }

        List<Integer> filterAxes = new ArrayList<>();

        for (var axis: axes) {
            var useSmudge = false;
            var isVerticalReflect = true;
            var colCheckSize = Math.min(axis, colSize - axis);

            for (var i = 0; i < colCheckSize; i++) {
                if (!isColumnEqual(axis - i, axis + i + 1)) {
                    if (!useSmudge &&
                            isColumnDifferentWithOneSymbol(axis-i, axis+i+1)) {
                        useSmudge = true;
                    } else {
                        isVerticalReflect = false;
                    }
                }
            }

            if (isVerticalReflect) {
                filterAxes.add(axis);
            }
        }

        return filterAxes;
    }


    List<Integer> getHorizontalAxisWithSmudge() {
        List<Integer> axes = new ArrayList<>();

        for (var i = rowSize-1; i > 0 ; i--) {
            if (isRowDifferentWithOneSymbol(i, i+1) || isRowEqual(i, i+1)) {
                axes.add(i);
            }
        }

        List<Integer> filterAxes = new ArrayList<>();

        for (var axis: axes) {
            var useSmudge = false;
            var isHorizontalReflect = true;
            var rowCheckSize = Math.min(axis, rowSize - axis);

            for (var i = 0; i < rowCheckSize; i++) {
                if (!isRowEqual(axis-i, axis+i+1)) {
                    if (!useSmudge &&
                            isRowDifferentWithOneSymbol(axis-i, axis+i+1)) {
                        useSmudge = true;
                    } else {
                        isHorizontalReflect = false;
                    }
                }
            }

            if (isHorizontalReflect) {
                filterAxes.add(axis);
            }
        }

        return filterAxes;
    }

    int getNumber() {
        var verticalAxis = getVerticalAxis();
        var horizontalAxis = getHorizontalAxis();

        return (verticalAxis != -1) ? verticalAxis :
                (horizontalAxis != -1) ? horizontalAxis * 100 : 0;
    }

    Integer getNumberWithSmudge() {
        var numbers = new ArrayList<>(getVerticalAxisWithSmudge());

        numbers.addAll(getHorizontalAxisWithSmudge().stream()
                .map(value -> value * 100)
                .toList());

        numbers.remove((Integer) getNumber());

        return Collections.max(numbers);
    }
}

public class LavaMirrorService {
    public static int getSumOfNotes(String fileName) {
        return Arrays.stream(FileService.readStringFromDocument(fileName)
                .split("\\n\\n"))
                .map(Pattern::new)
                .map(Pattern::getNumber)
                .reduce(0, Integer::sum);
    }

    public static int getSumOfNotesWithSmudge(String fileName) {
        return Arrays.stream(FileService.readStringFromDocument(fileName)
                        .split("\\n\\n"))
                .map(Pattern::new)
                .map(Pattern::getNumberWithSmudge)
                .reduce(0, Integer::sum);
    }
}
