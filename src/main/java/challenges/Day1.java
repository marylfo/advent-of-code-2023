package challenges;

import services.CalibrationValueRecoveryService;

public class Day1 {
    public static void solvingTrebuchet() {
        Integer sum = CalibrationValueRecoveryService.produceValueFromDocument("1-CalibrationDocument");
        System.out.println("Solving Day 1 Trebuchet: %s".formatted(sum));
    }
    public static void solvingTrebuchetWithLetterSpelled() {
        Integer sum = CalibrationValueRecoveryService.produceValueFromDocumentWithLetterSpelled("1-CalibrationDocument");
        System.out.println("Solving Day 1 Trebuchet With Letter Spelled: %s".formatted(sum));
    }
}
