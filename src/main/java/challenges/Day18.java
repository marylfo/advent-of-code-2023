package challenges;

import services.LavaLagoonService;

public class Day18 {
    public static void getCubicMeters() {
        var num = LavaLagoonService.getCubicMeters("18-Plan");
        System.out.printf("Solving Day 17 Cubic Meters: %s%n", num);
    }

    public static void getCubicMetersUsingColor() {
        var num = LavaLagoonService.getCubicMetersUsingColor("18-Plan");
        System.out.printf("Solving Day 17 Cubic Meters Using Color: %s%n", num);
    }

}
