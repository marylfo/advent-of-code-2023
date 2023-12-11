package challenges;

import services.PipeService;

public class Day10 {
    public static void solvingStepsToFarthestPoint() {
        var step = PipeService.getStepsToFarthestPoint("10-Grid");
        System.out.printf("Solving Day 10 Steps To Farthest Point: %s%n", step);
    }

    public static void solvingNumberOfEnclosedTiles() {
        var step = PipeService.getNumberOfEnclosedTiles("10-Grid");
        System.out.printf("Solving Day 10 Number Of Enclosed Tiles: %s%n", step);
    }
}
