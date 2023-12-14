package challenges;

import services.RockService;

public class Day14 {
    public static void solvingTotalLoad() {
        var total = RockService.getTotalLoad("14-Rock");
        System.out.printf("Solving Day 14 Total Load: %s%n", total);
    }

    public static void solvingTotalLoadWithCycle() {
        var total = RockService.getTotalLoadFromCycle("14-Rock");
        System.out.printf("Solving Day 14 Total Load With Cycle: %s%n", total);
    }
}
