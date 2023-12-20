package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Coordinate {
    private int row;
    private int col;
    private String edgeColour;

    Coordinate(int row, int col, String edgeColour) {
        this.row = row;
        this.col = col;
        this.edgeColour = edgeColour;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    void addRowOffset(int offset) {
        row = row + offset;
    }

    void addColOffset(int offset) {
        col = col + offset;
    }
}

class DigPlan {
    private List<Coordinate> coordinates;
    private int rowSize;
    private int colSize;
    private final char[][] grid;

    DigPlan(String fileName) {
        coordinates = new ArrayList<>();
        importCoordinates(fileName);
        normaliseCoordinates();
        setRowAndColSize();

        grid = new char[rowSize][colSize];

        setEdgesToGrid();
        setBottomOutFields();
        setOutFields();
    }

    void importCoordinates(String fileName) {
        List<String> lines = FileService.readStringFromDocument(fileName).lines().toList();

        int startRow = 0;
        int startCol = 0;

        coordinates.add(new Coordinate(startRow, startCol, ""));

        for (var line: lines) {
            var split = line.split(" ");
            var direction = split[0];
            var distance = Integer.parseInt(split[1]);
            var colour = split[2];

            if (direction.compareTo("R") == 0) {
                for (var i = 1; i <= distance; i++ ) {
                    coordinates.add(new Coordinate(startRow, ++startCol, colour));
                }
            } else if (direction.compareTo("L") == 0) {
                for (var i = 1; i <= distance; i++ ) {
                    coordinates.add(new Coordinate(startRow, --startCol, colour));
                }
            } else if (direction.compareTo("U") == 0) {
                for (var i = 1; i <= distance; i++ ) {
                    coordinates.add(new Coordinate(--startRow, startCol, colour));
                }
            } else if (direction.compareTo("D") == 0) {
                for (var i = 1; i <= distance; i++ ) {
                    coordinates.add(new Coordinate(++startRow, startCol, colour));
                }
            }
        }
    }

    void normaliseCoordinates() {
        int minRow = 0;
        int minCol = 0;

        for (var edge: coordinates) {
            minRow = Math.min(minRow, edge.getRow());
            minCol = Math.min(minCol, edge.getCol());
        }

        for (var edge: coordinates) {
            edge.addRowOffset(Math.abs(minRow));
            edge.addColOffset(Math.abs(minCol));
        }
    }

    void setRowAndColSize() {
        int row = 0;
        int col = 0;

        for (var coordinate: coordinates) {
            row = Math.max(row, coordinate.getRow());
            col = Math.max(col, coordinate.getCol());
        }

        rowSize = row + 1 + 1; //Give one extra row for the bottom to do flood fill
        colSize = col + 1;
    }

    void setEdgesToGrid() {
        for (var coordinate: coordinates) {
            grid[coordinate.getRow()][coordinate.getCol()] = '#';
        }
    }

    void setBottomOutFields() {
        Arrays.fill(grid[rowSize - 1], 'O');
    }

    void printGrid() {
        for (var i = 0; i < rowSize; i++) {
            String rowString = "";
            for (var j = 0; j < colSize; j++) {
                rowString += (grid[i][j] == '#') ? '#' :
                        (grid[i][j] == 'O') ? 'O' :
                                (grid[i][j] == 'I') ? 'I' : '.';
            }
            System.out.println(rowString);
        }
    }

    void floodFill(int rowIndex, int colIndex) {
        if (rowIndex < 0 || rowIndex >= rowSize ||
                colIndex < 0 || colIndex >= colSize||
                grid[rowIndex][colIndex] == '#' ||
                grid[rowIndex][colIndex] == 'O') {
            return;
        }

        grid[rowIndex][colIndex] = 'O';

        floodFill(rowIndex+1, colIndex);
        floodFill(rowIndex-1, colIndex);
        floodFill(rowIndex, colIndex+1);
        floodFill(rowIndex, colIndex-1);
    }

    int getLastEdgeCoordinateFromRow(int row) {
        int col = -1;
        for (var j = 0; j < colSize; j++) {
            if (grid[row][j] == '#') {
                col = j;
            }
        }

        return col;
    }

    void setOutFields() {
        boolean isMeetingEdge;

        for (var i = 0; i < rowSize - 1; i++) {
            isMeetingEdge = false;
            int j = 0;

            while (!isMeetingEdge) {
                if (grid[i][j] == '#') {
                    isMeetingEdge = true;
                } else {
                    grid[i][j++] = 'O';
                }
            }

            for (var k = getLastEdgeCoordinateFromRow(i) + 1; k < colSize; k++) {
                grid[i][k] = 'O';
            }
        }

        for (var i = 0; i < rowSize; i++) {
            for (var j = 0; j < colSize; j++) {
                if (grid[i][j] == 'O'){
                    if (i > 0 && grid[i-1][j] == '\u0000') {
                        floodFill(i-1, j);
                    }
                    if (i < (rowSize-1) && grid[i+1][j] == '\u0000') {
                        floodFill(i+1, j);
                    }
                    if (j > 0 && grid[i][j-1] == '\u0000') {
                        floodFill(i, j-1);
                    }
                    if (j < (colSize-1) && grid[i][j+1] == '\u0000') {
                        floodFill(i, j+1);
                    }
                }
            }
        }
    }

    int calculateArea() {
        int area = 0;

        for (var i = 0; i < rowSize; i++) {
            for (var j = 0; j < colSize; j++) {
                if (grid[i][j] != 'O'){
                    area++;
                }
            }
        }

        return area;
    }
}

public class LavaLagoonService {
    public static int getCubicMeters(String fileName) {
        var digPlan = new DigPlan(fileName);
        return digPlan.calculateArea();
    }

    private static int getXOffset(int dir) {
        return switch (dir) {
            case 0 -> 1;
            case 2 -> -1;
            default -> 0;
        };
    }

    private static int getYOffset(int dir) {
        return switch (dir) {
            case 1 -> 1;
            case 3 -> -1;
            default -> 0;
        };
    }

    private static long getCubicMetersFromHexadecimalValues(List<String> instructions) {
        long xPos = 0;
        double area = 1;

        for (var instruction: instructions) {
            long steps = Integer.parseInt(instruction.substring(0, instruction.length() - 1), 16);
            int dir = Integer.parseInt(instruction.substring(instruction.length() - 1));

            xPos += getXOffset(dir) * steps;
            area += (getYOffset(dir) * steps) * xPos + (double) steps /2;
        }

        return Math.round(area);
    }

    public static long getCubicMetersUsingColor(String fileName) {
        var instructions = FileService.readStringFromDocument(fileName)
                .lines()
                .map(line -> line.substring(line.indexOf('#') + 1, line.length() - 1))
                .toList();

        return getCubicMetersFromHexadecimalValues(instructions);
    }
}
