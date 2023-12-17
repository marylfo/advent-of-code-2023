package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

enum LightTileType {
    VERTICAL_SPLITTER('|'),
    HORIZONTAL_SPLITTER('-'),
    FORWARD_MIRROR('/'),
    BACKWARD_MIRROR('\\'),
    EMPTY('.');

    final char value;

    LightTileType(Character value) {
        this.value = value;
    }

    public static LightTileType from(Character input) {
        for (LightTileType type: LightTileType.values()) {
            if (type.value == input) {
                return type;
            }
        }
        return null;
    }
}

class LightTile {
    private final LightTileType type;
    private LightTile upTile;
    private LightTile downTile;
    private LightTile leftTile;
    private LightTile rightTile;

    LightTile(Character type) {
        this.type = LightTileType.from(type);
        this.upTile = null;
        this.downTile = null;
        this.leftTile = null;
        this.rightTile = null;
    }

    public void setUpTile(LightTile tile) {
        this.upTile = tile;
    }

    public void setDownTile(LightTile tile) {
        this.downTile = tile;
    }

    public void setLeftTile(LightTile tile) {
        this.leftTile = tile;
    }

    public void setRightTile(LightTile tile) {
        this.rightTile = tile;
    }

    @Override
    public String toString() {
        return "%c".formatted(type.value);
    }

    boolean hasOneOutDir(Direction inDir) {
        if (type == LightTileType.HORIZONTAL_SPLITTER && (
                inDir == Direction.UP || inDir == Direction.DOWN
        )) {
            return false;
        } else if (type == LightTileType.VERTICAL_SPLITTER && (
                inDir == Direction.LEFT || inDir == Direction.RIGHT
        )) {
            return false;
        }

        return true;
    }

    boolean isMirror() {
        return type == LightTileType.FORWARD_MIRROR ||
                type == LightTileType.BACKWARD_MIRROR;
    }

    boolean isEmptyTile() {
        return type == LightTileType.EMPTY;
    }

    Direction getFirstOutDir(Direction inDir) {
        if (type == LightTileType.VERTICAL_SPLITTER &&
                (inDir == Direction.LEFT || inDir == Direction.RIGHT)) {
            return Direction.UP;
        }

        if (type == LightTileType.HORIZONTAL_SPLITTER &&
                (inDir == Direction.UP || inDir == Direction.DOWN)) {
            return Direction.LEFT;
        }

        return null;
    }

    Direction getSecondOutDir(Direction inDir) {
        if (type == LightTileType.VERTICAL_SPLITTER &&
                (inDir == Direction.LEFT || inDir == Direction.RIGHT)) {
            return Direction.DOWN;
        }

        if (type == LightTileType.HORIZONTAL_SPLITTER &&
                (inDir == Direction.UP || inDir == Direction.DOWN)) {
            return Direction.RIGHT;
        }

        return null;
    }

    Direction getOneOutDir(Direction inDir) {
        if (type == LightTileType.EMPTY ||
                type == LightTileType.HORIZONTAL_SPLITTER ||
                type == LightTileType.VERTICAL_SPLITTER
        ) {
            return inDir;
        }

        if (type == LightTileType.FORWARD_MIRROR) {
            if (inDir == Direction.UP) {
                return Direction.RIGHT;
            } else if (inDir == Direction.LEFT) {
                return Direction.DOWN;
            } else if (inDir == Direction.RIGHT) {
                return Direction.UP;
            } else {
                return Direction.LEFT;
            }
        }

        if (type == LightTileType.BACKWARD_MIRROR) {
            if (inDir == Direction.UP) {
                return Direction.LEFT;
            } else if (inDir == Direction.LEFT) {
                return Direction.UP;
            } else if (inDir == Direction.RIGHT) {
                return Direction.DOWN;
            } else {
                return Direction.RIGHT;
            }
        }

        return null;
    }

    LightTile getNextTile(Direction outDir) {
        return switch (outDir) {
            case UP -> upTile;
            case LEFT -> leftTile;
            case RIGHT -> rightTile;
            case DOWN -> downTile;
        };
    }
}

class LightGrid {
    private final List<List<LightTile>> layout;
    private Set<LightTile> energizedTiles;
    private final int row;
    private final int column;

    public LightGrid(String fileName) {
        layout = new ArrayList<>();
        energizedTiles = new HashSet<>();

        importLayout(fileName);

        row = layout.size();
        column = layout.get(0).size();

        setNeighbourTile();
    }

    private void importLayout(String fileName) {
        var raw = FileService.readStringFromDocument(fileName);

        for (var line : raw.lines().toList()) {
            layout.add(
                    Arrays.stream(line.split(""))
                            .map(str -> str.charAt(0))
                            .map(LightTile::new)
                            .toList()
            );
        }
    }

    private LightTile getTileAt(int row, int column) {
        return layout.get(row).get(column);
    }

    private void setNeighbourTile() {
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

    void startRecordEnergizedTiles(int row, int col, Direction startDir) {
        recordEnergizedTiles(startDir, getTileAt(row, col));
    }

    void recordEnergizedTiles(Direction inDir, LightTile currentTile) {
        if (currentTile.isMirror() || (
                !energizedTiles.contains(currentTile)) ||
                (energizedTiles.contains(currentTile) && currentTile.isEmptyTile())
        ) {

            energizedTiles.add(currentTile);

            if (currentTile.hasOneOutDir(inDir)) {
                var outDir = currentTile.getOneOutDir(inDir);
                var nextTile = currentTile.getNextTile(outDir);
                if (nextTile != null) {
                    recordEnergizedTiles(outDir, nextTile);
                }
            } else {
                var outDir1 = currentTile.getFirstOutDir(inDir);
                var nextTile1 = currentTile.getNextTile(outDir1);
                if (nextTile1 != null) {
                    recordEnergizedTiles(outDir1, nextTile1);
                }

                var outDir2 = currentTile.getSecondOutDir(inDir);
                var nextTile2 = currentTile.getNextTile(outDir2);
                if (nextTile2 != null) {
                    recordEnergizedTiles(outDir2, nextTile2);
                }
            }
        }
    }

    int getNumberOfEnergizedTiles() {
        energizedTiles.clear();
        startRecordEnergizedTiles(0, 0, Direction.RIGHT);
        return energizedTiles.size();
    }

    int getLargestNumberOfEnergizedTiles() {
        int max = 0;

        // first row with inDir = down
        for (var i = 0; i < column; i++) {
            energizedTiles.clear();
            startRecordEnergizedTiles(0, i, Direction.DOWN);
            max = Math.max(energizedTiles.size(), max);
        }

        // last row with inDir = up
        for (var i = 0; i < column; i++) {
            energizedTiles.clear();
            startRecordEnergizedTiles(row-1, i, Direction.UP);
            max = Math.max(energizedTiles.size(), max);
        }

        // first column with inDir = RIGHT
        for (var i = 0; i < row; i++) {
            energizedTiles.clear();
            startRecordEnergizedTiles(i, 0, Direction.RIGHT);
            max = Math.max(energizedTiles.size(), max);
        }

        // last column with inDir = left
        for (var i = 0; i < row; i++) {
            energizedTiles.clear();
            startRecordEnergizedTiles(i, column-1, Direction.LEFT);
            max = Math.max(energizedTiles.size(), max);
        }

        return max;
    }
}

public class BeamService {
    public static int getNumberOfEnergizedTiles(String fileName) {
        var grid = new LightGrid(fileName);
        return grid.getNumberOfEnergizedTiles();
    }

    public static int getLargestNumberOfEnergizedTiles(String fileName) {
        var grid = new LightGrid(fileName);
        return grid.getLargestNumberOfEnergizedTiles();
    }
}
