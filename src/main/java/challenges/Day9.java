package challenges;

import services.OasisReportService;

public class Day9 {
    public static void solvingSumOfExtrapolatedValuesAtEnd() {
        var sum = OasisReportService.getSumOfExtrapolatedValuesAtEnd("9-Report");
        System.out.printf("Solving Day 9 Sum Of Extrapolated Values At End: %s%n", sum);
    }
    public static void solvingSumOfExtrapolatedValuesAtStart() {
        var sum = OasisReportService.getSumOfExtrapolatedValuesAtStart("9-Report");
        System.out.printf("Solving Day 9 Sum Of Extrapolated Values At Start: %s%n", sum);
    }
}
