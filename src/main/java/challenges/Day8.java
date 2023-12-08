package challenges;

import services.MapNetworkService;

public class Day8 {
    public static void solvingNumOfSteps() {
        var num = MapNetworkService.getNumOfSteps("8-Map");
        System.out.printf("Solving Day 8 Number Of Step: %s%n", num);
    }
    public static void solvingNumOfSimultaneouslySteps() {
        var num = MapNetworkService.getNumOfSimultaneouslySteps("8-Map");
        System.out.printf("Solving Day 8 Number Of Simultaneously Steps: %s%n", num);
    }
}
