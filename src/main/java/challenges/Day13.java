package challenges;

import services.LavaMirrorService;

public class Day13 {
    public static void solvingSummarizingNumber() {
        var sum = LavaMirrorService.getSumOfNotes("13-Lava");
        System.out.printf("Solving Day 13 Sum Of Notes: %s%n", sum);
    }

    public static void solvingSummarizingNumberWithSmudge() {
        var sum = LavaMirrorService.getSumOfNotesWithSmudge("13-Lava");
        System.out.printf("Solving Day 13 Sum Of Notes WithSmudge: %s%n", sum);
    }
}
