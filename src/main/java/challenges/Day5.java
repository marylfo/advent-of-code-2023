package challenges;

import services.AlmanacService;

public class Day5 {
    public static void solvingLowestLocationNumber() {
        long value = AlmanacService.getLowestLocationNumber("5-Almanac");
        System.out.printf("Solving Day 5 Lowest Location Number: %s%n", value);
    }

    public static void solvingLowestLocationNumberWithRangeSeed() {
        long value = AlmanacService.getLowestLocationNumberWithRangeSeed("5-Almanac");
        System.out.printf("Solving Day 5 Lowest Location Number With Range Seed: %s%n", value);
    }

}
