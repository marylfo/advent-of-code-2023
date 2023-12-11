package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum Direction {
    UP,
    LEFT,
    RIGHT,
    DOWN
}

enum TileType {
    VERTICAL('|'),
    HORIZONTAL('-'),
    N_E('L'),
    N_W('J'),
    S_W('7'),
    S_E('F'),
    GROUND('.'),
    START('S');

    final char value;
    TileType(Character value) {
        this.value = value;
    }

    public static TileType from(Character input) {
        for (TileType type: TileType.values()) {
            if (type.value == input) {
                return type;
            }
        }
        return null;
    }
}

enum TileLocation {
    BOARDER('B'),
    UNKNOWN('U'),
    LEFT('L'),
    RIGHT('R');

    final char value;
    TileLocation(Character value) {
        this.value = value;
    }

    public static TileLocation from(Character input) {
        for (TileLocation location: TileLocation.values()) {
            if (location.value == input) {
                return location;
            }
        }
        return TileLocation.UNKNOWN;
    }
}

class Tile {
    private final TileType type;
    private Tile upTile;
    private Tile downTile;
    private Tile leftTile;
    private Tile rightTile;

    Tile(Character type) {
        this.type = TileType.from(type);
        this.upTile = null;
        this.downTile = null;
        this.leftTile = null;
        this.rightTile = null;
    }

    public void setUpTile(Tile tile) {
        this.upTile = tile;
    }
    public void setDownTile(Tile tile) {
        this.downTile = tile;
    }
    public void setLeftTile(Tile tile) {
        this.leftTile = tile;
    }
    public void setRightTile(Tile tile) {
        this.rightTile = tile;
    }

    boolean isAbleToMoveDown() {
        return (downTile != null) && (downTile.type == TileType.VERTICAL ||
                downTile.type == TileType.N_E ||
                downTile.type == TileType.N_W);
    }

    boolean isAbleToMoveRight() {
        return (rightTile != null) && (rightTile.type == TileType.HORIZONTAL ||
                rightTile.type == TileType.S_W ||
                rightTile.type == TileType.N_W);
    }
    boolean isAbleToMoveLeft() {
        return (leftTile != null) && (leftTile.type == TileType.HORIZONTAL ||
                leftTile.type == TileType.N_E ||
                leftTile.type == TileType.S_E);
    }

    public Tile getNextTile(Direction direction) {
        return switch (direction) {
            case UP -> upTile;
            case DOWN -> downTile;
            case LEFT -> leftTile;
            default -> rightTile;
        };
    }

    public Direction getNextDirection(Direction... currDirection) {
        if (type.compareTo(TileType.START) == 0) {
            if (isAbleToMoveRight()) {
                return Direction.RIGHT;
            } else if (isAbleToMoveDown()) {
                return Direction.DOWN;
            } else if (isAbleToMoveLeft()) {
                return Direction.LEFT;
            } else {
                return Direction.UP;
            }
        } else if (type.compareTo(TileType.VERTICAL) == 0) {
            if (currDirection[0] == Direction.DOWN) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        } else if (type.compareTo(TileType.HORIZONTAL) == 0) {
            if (currDirection[0] == Direction.LEFT) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        } else if (type.compareTo(TileType.S_E) == 0) {
            if (currDirection[0] == Direction.UP) {
                return Direction.RIGHT;
            } else {
                return Direction.DOWN;
            }
        } else if (type.compareTo(TileType.S_W) == 0) {
            if (currDirection[0] == Direction.RIGHT) {
                return Direction.DOWN;
            } else {
                return Direction.LEFT;
            }
        } else if (type.compareTo(TileType.N_W) == 0) {
            if (currDirection[0] == Direction.DOWN) {
                return Direction.LEFT;
            } else {
                return Direction.UP;
            }
        } else {
            if (currDirection[0] == Direction.LEFT) {
                return Direction.UP;
            } else {
                return Direction.RIGHT;
            }
        }
    }

    public boolean isStartTile() {
        return type.compareTo(TileType.START) == 0;
    }

    public boolean isGround() {
        return type.compareTo(TileType.GROUND) == 0;
    }

    @Override
    public String toString() {
        return "%c".formatted(type.value);
    }
}

class Pipe {
    private final List<List<Tile>> pipeGrid;
    private final List<Integer> locationOfS;
    private Direction outDirectionsOfS;
    private Direction inDirectionsOfS;
    private final int row;
    private final int column;
    private final TileLocation[][] locationGrid;

    public Pipe(String fileName) {
        pipeGrid = new ArrayList<>();
        locationOfS = new ArrayList<>();

        importPipeGrid(fileName);

        row = pipeGrid.size();
        column = pipeGrid.get(0).size();

        setNeighbourPipe();

        // For Part 2
        locationGrid = new TileLocation[row][column];
    }

    void assignLeftLocation(int rowIndex, int colIndex) {
        if ((rowIndex >=0 && rowIndex < row) &&
                (colIndex >= 0 && colIndex <column) &&
                locationGrid[rowIndex][colIndex] == TileLocation.UNKNOWN) {
            locationGrid[rowIndex][colIndex] = TileLocation.LEFT;
        }
    }

    void assignRightLocation(int rowIndex, int colIndex) {
        if ((rowIndex >=0 && rowIndex < row) &&
                (colIndex >= 0 && colIndex <column) &&
                locationGrid[rowIndex][colIndex] == TileLocation.UNKNOWN) {
            locationGrid[rowIndex][colIndex] = TileLocation.RIGHT;
        }
    }

    void setLeftAndRightToLocationGrid() {
        var inDir = inDirectionsOfS;
        var outDir = outDirectionsOfS;
        var currRow = locationOfS.get(0);
        var currCol = locationOfS.get(1);
        var currTile = getTileAt(currRow, currCol);

        do {
            if (inDir == Direction.UP && outDir == Direction.RIGHT) {
                assignLeftLocation(currRow, currCol-1);
                assignLeftLocation(currRow-1, currCol);
                assignLeftLocation(currRow-1, currCol-1);
                assignRightLocation(currRow+1, currCol+1);
            } else if (inDir == Direction.RIGHT && outDir == Direction.RIGHT) {
                assignLeftLocation(currRow-1, currCol);
                assignRightLocation(currRow+1, currCol);
            } else if (inDir == Direction.RIGHT && outDir == Direction.DOWN) {
                assignLeftLocation(currRow-1, currCol);
                assignLeftLocation(currRow-1, currCol+1);
                assignLeftLocation(currRow, currCol+1);
                assignRightLocation(currRow+1, currCol-1);
            } else if (inDir == Direction.DOWN && outDir == Direction.DOWN) {
                assignLeftLocation(currRow, currCol+1);
                assignRightLocation(currRow, currCol-1);
            } else if (inDir == Direction.DOWN && outDir == Direction.LEFT) {
                assignLeftLocation(currRow, currCol+1);
                assignLeftLocation(currRow+1, currCol);
                assignLeftLocation(currRow+1, currCol+1);
                assignRightLocation(currRow-1, currCol-1);
            } else if (inDir == Direction.LEFT && outDir == Direction.LEFT) {
                assignLeftLocation(currRow+1, currCol);
                assignRightLocation(currRow+1, currCol);
            } else if (inDir == Direction.LEFT && outDir == Direction.UP) {
                assignLeftLocation(currRow+1, currCol);
                assignLeftLocation(currRow+1, currCol-1);
                assignLeftLocation(currRow, currCol-1);
                assignRightLocation(currRow-1, currCol+1);
            } else if (inDir == Direction.UP && outDir == Direction.UP) {
                assignLeftLocation(currRow, currCol-1);
                assignRightLocation(currRow, currCol+1);
            }

            if (outDir == Direction.RIGHT) {
                currCol++;
            } else if (outDir == Direction.LEFT) {
                currCol--;
            } else if (outDir == Direction.UP) {
                currRow--;
            } else {
                currRow++;
            }

            currTile = getTileAt(currRow, currCol);
            inDir = outDir;
            outDir = currTile.getNextDirection(inDir);
        } while (!currTile.isStartTile());
    }

    void fillUnknownTiles() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (locationGrid[i][j] == TileLocation.LEFT) {
                    floodFill(i+1, j, TileLocation.LEFT);
                    floodFill(i-1, j, TileLocation.LEFT);
                    floodFill(i, j+1, TileLocation.LEFT);
                    floodFill(i, j-1, TileLocation.LEFT);
                }

                if (locationGrid[i][j] == TileLocation.RIGHT) {
                    floodFill(i+1, j, TileLocation.RIGHT);
                    floodFill(i-1, j, TileLocation.RIGHT);
                    floodFill(i, j+1, TileLocation.RIGHT);
                    floodFill(i, j-1, TileLocation.RIGHT);
                }
            }
        }
    }

    void floodFill(int rowIndex, int colIndex, TileLocation location) {
        if (rowIndex < 0 || rowIndex >= row ||
                colIndex < 0 || colIndex >= column||
                locationGrid[rowIndex][colIndex] == TileLocation.LEFT ||
                locationGrid[rowIndex][colIndex] == TileLocation.RIGHT ||
                locationGrid[rowIndex][colIndex] == TileLocation.BOARDER) {
            return;
        }

        locationGrid[rowIndex][colIndex] = location;

        floodFill(rowIndex+1, colIndex, location);
        floodFill(rowIndex-1, colIndex, location);
        floodFill(rowIndex, colIndex+1, location);
        floodFill(rowIndex, colIndex-1, location);
    }

    void printLocationGrid() {
        for (var i = 0; i < row; i++) {
            String rowString = "";
            for (var j = 0; j < column; j++) {
                rowString += (locationGrid[i][j] == null ? 'N' : locationGrid[i][j].value);
            }
            System.out.println(rowString);
        }
    }

    private void importPipeGrid(String fileName) {
        var raw = FileService.readStringFromDocument(fileName);
        var row = 0;
        var isStartFound = false;

        for (var line : raw.lines().toList()) {
            pipeGrid.add(
                    Arrays.stream(line.split(""))
                            .map(str -> str.charAt(0))
                            .map(Tile::new)
                            .toList()
            );

            var location = line.indexOf("S");
            if (location != -1 && !isStartFound) {
                locationOfS.add(row);
                locationOfS.add(location);
                isStartFound = true;
            }

            row++;
        }
    }

    private Tile getTileAt(int row, int column) {
        return pipeGrid.get(row).get(column);
    }

    private void setNeighbourPipe() {
        for (var i = 0; i < row; i++) {
            for (var j = 0; j < column; j++) {
                var currTile = getTileAt(i, j);
                if (i != 0) {
                    currTile.setUpTile(getTileAt(i-1, j));
                }
                if (i != (row-1)) {
                    currTile.setDownTile(getTileAt(i+1, j));
                }
                if (j != 0) {
                    currTile.setLeftTile(getTileAt(i, j-1));
                }
                if (j != (column-1)) {
                    currTile.setRightTile(getTileAt(i, j+1));
                }
            }
        }
    }

    public int getStepsToFarthestPoint() {
        Tile startTile = getTileAt(locationOfS.get(0), locationOfS.get(1));
        var direction = startTile.getNextDirection();

        Tile currentTile = startTile.getNextTile(direction);
        int lengthOfLoop = 1;

        while (!currentTile.isStartTile()) {
            lengthOfLoop++;
            var nextDirection = currentTile.getNextDirection(direction);
            currentTile = currentTile.getNextTile(nextDirection);

            direction = nextDirection;
        }

        return Math.floorDiv(lengthOfLoop, 2);
    }

    private void setPipeBorderToLocationGrid() {
        Tile startTile = getTileAt(locationOfS.get(0), locationOfS.get(1));
        var direction = startTile.getNextDirection();

        // Set start point as broader
        int currX = locationOfS.get(0);
        int currY = locationOfS.get(1);
        locationGrid[currX][currY] = TileLocation.from('B');

        Tile currentTile = startTile.getNextTile(direction);
        outDirectionsOfS = direction;

        while (!currentTile.isStartTile()) {
            // Set current point as broader
            if (direction == Direction.UP) {
                currX--;
            } else if (direction == Direction.DOWN) {
                currX++;
            } else if (direction == Direction.LEFT) {
                currY--;
            } else {
                currY++;
            }
            locationGrid[currX][currY] = TileLocation.from('B');

            var nextDirection = currentTile.getNextDirection(direction);
            if (currentTile.getNextTile(nextDirection).isStartTile()) {
                inDirectionsOfS = nextDirection;
            }
            currentTile = currentTile.getNextTile(nextDirection);

            direction = nextDirection;
        }
    }

    private void labelGroundAndNotUsedTileToLocationGrid() {
        for (var i = 0; i < row; i++) {
            for (var j = 0; j < column; j++) {
                if (pipeGrid.get(i).get(j).isGround()) {
                    locationGrid[i][j] = TileLocation.from('U');
                }
                
                if (locationGrid[i][j] == null) {
                    locationGrid[i][j] = TileLocation.from('U');
                }
            }
        }
    }

    public int getNumOfInnerGround() {
        setPipeBorderToLocationGrid();
        labelGroundAndNotUsedTileToLocationGrid();
        setLeftAndRightToLocationGrid();
        fillUnknownTiles();

        int leftCount = 0, rightCount = 0, unknownCount = 0;
        for (var i = 0; i < row; i++) {
            for (var j = 0; j < column; j++) {
                if (locationGrid[i][j] == TileLocation.LEFT) {
                    leftCount++;
                } else if (locationGrid[i][j] == TileLocation.RIGHT) {
                    rightCount++;
                } else if (locationGrid[i][j] == TileLocation.UNKNOWN) {
                    unknownCount++;
                }
            }
        }

        return Math.min(leftCount, rightCount) + unknownCount;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        pipeGrid.forEach(row -> {
            StringBuilder line = new StringBuilder();
            for (var tile: row) {
                line.append(tile.toString());
            }
            line.append('\n');
            output.append(line);
        });
        return output.toString();
    }
}

public class PipeService {
    public static int getStepsToFarthestPoint(String fileName) {
        var pipe = new Pipe(fileName);
        return pipe.getStepsToFarthestPoint();
    }

    public static int getNumberOfEnclosedTiles(String fileName) {
        var pipe = new Pipe(fileName);
        return pipe.getNumOfInnerGround();
    }
}
