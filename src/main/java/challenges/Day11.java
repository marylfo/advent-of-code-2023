package challenges;

import services.GalaxyService;

public class Day11 {
    public static void solvingSumOfLengths() {
        var step = GalaxyService.getSumOfLengths("11-Galaxy", 1);
        System.out.printf("Solving Day 11 Sum Of The Lengths: %s%n", step);
    }

    public static void solvingSumOfLengths_1000000() {
        var step = GalaxyService.getSumOfLengths("11-Galaxy", 1000000);
        System.out.printf("Solving Day 11 Sum Of The Lengths (1000000 larger): %s%n", step);
    }
}
