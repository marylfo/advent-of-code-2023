package challenges;

import services.BoatRaceService;

public class Day6 {
    public static void solvingMultiply() {
        var multiply = BoatRaceService.getMultiplyOfNumOfWay("6-Race");
        System.out.printf("Solving Day 6 Multiply: %s%n", multiply);
    }

    public static void solvingNumOfWay() {
        var num = BoatRaceService.getNumOfWay("6-Race");
        System.out.printf("Solving Day 6 Number: %s%n", num);
    }
}
