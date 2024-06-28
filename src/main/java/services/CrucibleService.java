package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

// Reference: https://youtu.be/2pDSooPLLkI

record HeatMapStep (int currRow, int currCol, int deltaRow, int deltaCol, int step) {

    public boolean isStart() {
        return (this.deltaRow() == 0 && this.deltaCol() == 0);
    }

    public boolean isDestination(int rowSize, int colSize) {
        return this.currRow() == (rowSize - 1) && this.currCol() == (colSize - 1);
    }

    public boolean isNotSameDirection(HeatMapDirection dir) {
        return !(dir.deltaRow() == this.deltaRow() && dir.deltaCol() == this.deltaCol());
    }
    public boolean isNotOppositeDirection(HeatMapDirection dir) {
        return !(dir.deltaRow() == -this.deltaRow() && dir.deltaCol() == -this.deltaCol());
    }
}

record HeatMapDirection(int deltaRow, int deltaCol) {}

record StepState(int heatLoss, HeatMapStep step) implements Comparable<StepState> {
    @Override
    public int compareTo(StepState other) {
        return Integer.compare(this.heatLoss, other.heatLoss);
    }
}

public class CrucibleService {

    static List<HeatMapDirection> directions = new ArrayList<>();

    static {
        directions.add(new HeatMapDirection(0,1));
        directions.add(new HeatMapDirection(1,0));
        directions.add(new HeatMapDirection(0,-1));
        directions.add(new HeatMapDirection(-1,0));
    }

    private final List<int[]> map;
    private final int rowSize;
    private final int colSize;

    private final HashSet<HeatMapStep> visitedSteps = new HashSet<>();
    private final PriorityQueue<StepState> pq = new PriorityQueue<>();

    public CrucibleService(String fileName) {
        map = FileService.readStringFromDocument(fileName)
                .lines()
                .map(line -> Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray())
                .toList();

        rowSize = map.size();
        colSize = map.get(0).length;
    }

    private boolean isValidPoint(int nextRow, int nextCol) {
        return nextRow >= 0 && nextRow < rowSize && nextCol >= 0 && nextCol < colSize;
    }

    private void resetQueueAndSet() {
        visitedSteps.clear();

        pq.clear();
        pq.add(new StepState(0, new HeatMapStep(0,0,0,0,0)));
    }

    private void moveInSameDirection(StepState stepState) {
        int currentHeatLoss = stepState.heatLoss();
        HeatMapStep currentStep = stepState.step();

        int nextRow = currentStep.currRow() + currentStep.deltaRow();
        int nextCol = currentStep.currCol() + currentStep.deltaCol();

        if (isValidPoint(nextRow, nextCol)) {
            pq.add(
                    new StepState(currentHeatLoss + map.get(nextRow)[nextCol],
                            new HeatMapStep(
                                    nextRow,
                                    nextCol,
                                    currentStep.deltaRow(),
                                    currentStep.deltaCol(),
                                    currentStep.step() + 1
                            )
                    )
            );
        }
    }

    private void moveInDifferentDirection(StepState stepState, HeatMapDirection nextDirection) {
        int currentHeatLoss = stepState.heatLoss();
        HeatMapStep currentStep = stepState.step();

        int nextRow = currentStep.currRow() + nextDirection.deltaRow();
        int nextCol = currentStep.currCol() + nextDirection.deltaCol();

        if (isValidPoint(nextRow, nextCol)) {
            pq.add(
                    new StepState(currentHeatLoss + map.get(nextRow)[nextCol],
                            new HeatMapStep(
                                    nextRow,
                                    nextCol,
                                    nextDirection.deltaRow(),
                                    nextDirection.deltaCol(),
                                    1
                            )
                    )
            );
        }
    }

    public int getMinHeatLoss() {
        resetQueueAndSet();

        while (!pq.isEmpty()) {
            var currentQueue = pq.remove();
            var currentHeatLoss = currentQueue.heatLoss();
            var currentStep = currentQueue.step();

            if (currentStep.isDestination(rowSize, colSize)) {
                return currentHeatLoss;
            }

            if (visitedSteps.contains(currentStep)) {
                continue;
            }

            visitedSteps.add(currentStep);

            if (currentStep.step() < 3 && !currentStep.isStart()) {
                moveInSameDirection(currentQueue);
            }

            for (var nextDirection : directions) {
                if (currentStep.isNotSameDirection(nextDirection) && currentStep.isNotOppositeDirection(nextDirection)) {
                    moveInDifferentDirection(currentQueue, nextDirection);
                }
            }
        }

        return -1;
    }

    public int getMinHeatLossUltra() {
        resetQueueAndSet();

        while (!pq.isEmpty()) {
            var currentQueue = pq.remove();
            var currentHeatLoss = currentQueue.heatLoss();
            var currentStep = currentQueue.step();

            if (currentStep.isDestination(rowSize, colSize)) {
                if (currentStep.step() >= 4) {
                    return currentHeatLoss;
                } else {
                    continue;
                }
            }

            if (visitedSteps.contains(currentStep)) {
                continue;
            }

            visitedSteps.add(currentStep);

            if (currentStep.step() < 10 && !currentStep.isStart()) {
                moveInSameDirection(currentQueue);
            }

            for (var nextDirection : directions) {
                if ((currentStep.isStart() && currentStep.step() == 0) ||
                        (currentStep.isNotSameDirection(nextDirection) &&
                                currentStep.isNotOppositeDirection(nextDirection) &&
                                currentStep.step() >= 4 && currentStep.step() <= 10
                        )
                ) {
                    moveInDifferentDirection(currentQueue, nextDirection);
                }
            }
        }

        return -1;
    }
}
