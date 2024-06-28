package challenges;

import services.CrucibleService;

public class Day17 {
    public static void getMinimizeHeatLoss() {
        var crucibleService = new CrucibleService("17-Map");
        var num = crucibleService.getMinHeatLoss();
        System.out.printf("Solving Day 17 Minimize Heat Loss: %s%n", num);
    }

    public static void getMinimizeHeatLossUltra() {
        var crucibleService = new CrucibleService("17-Map");
        var num = crucibleService.getMinHeatLossUltra();
        System.out.printf("Solving Day 17 Minimize Heat Loss Ultra: %s%n", num);
    }
}
