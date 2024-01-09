package challenges;

import services.XmasWorkflowService;

public class Day19 {
    public static void getSumOfRatingNumbers() {
        var num = XmasWorkflowService.getSumOfRatingNumbers("19-puzzle");
        System.out.printf("Solving Day 19 Sum of Rating: %s%n", num);
    }

    public static void getDistinctCombo() {
        var num = XmasWorkflowService.getDistinctCombo("19-puzzle");
        System.out.printf("Solving Day 19 Distinct Combo: %s%n", num);
    }
}
