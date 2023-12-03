package challenges;

import services.EngineSchematicService;

public class Day3 {
    public static void solvingSumOfPartNumbers() {
        Integer sum = EngineSchematicService.getSumOfPartNumbers("3-EngineSchematic");
        System.out.printf("Solving Day 3 Sum Of Part Numbers: %s%n", sum);
    }

    public static void solvingSumOfGearRations() {
        Integer sum = EngineSchematicService.getSumOfGearRations("3-EngineSchematic");
        System.out.printf("Solving Day 3 Sum Of Gear Rations: %s%n", sum);
    }
}
