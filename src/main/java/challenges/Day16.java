package challenges;

import services.BeamService;

public class Day16 {
    public static void getNumberOfEnergizedTiles() {
        var num = BeamService.getNumberOfEnergizedTiles("16-Layout");
        System.out.printf("Solving Day 16 Number: %s%n", num);
    }

    public static void getLargestNumberOfEnergizedTiles() {
        var num = BeamService.getLargestNumberOfEnergizedTiles("16-Layout");
        System.out.printf("Solving Day 16 Largest Number: %s%n", num);
    }
}
