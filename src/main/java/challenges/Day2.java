package challenges;

import services.CubeConundrumService;

public class Day2 {
    public static void solvingCubeConundrum() {
        Integer sum = CubeConundrumService.getSumOfPossibleGameIds("2-CubeGameData");
        System.out.printf("Solving Day 2 Cube Conundrum: %s%n", sum);
    }

    public static void solvingCubeConundrumWithPower() {
        Integer sum = CubeConundrumService.getSumOfPowerOfGames("2-CubeGameData");
        System.out.printf("Solving Day 2 Cube Conundrum Sum Of Power: %s%n", sum);
    }
}
