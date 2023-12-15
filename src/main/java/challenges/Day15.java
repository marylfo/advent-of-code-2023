package challenges;

import services.HolidayHashService;

public class Day15 {
    public static void solvingSum() {
        var sum = HolidayHashService.getSum("15-Sequence");
        System.out.printf("Solving Day 15 Sum: %s%n", sum);
    }

    public static void solvingFocusPower() {
        var sum = HolidayHashService.getFocusPower("15-Sequence");
        System.out.printf("Solving Day 15 Focus Power: %s%n", sum);
    }
}
