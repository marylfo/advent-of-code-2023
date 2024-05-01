package challenges;

import services.SpringService;

public class Day12 {
        public static void solvingSumOfLengths() {
            var sum = SpringService.getSumOfPossibleComboRecursion("12-Springs");
            System.out.printf("Solving Day 12 Sum Of Counts: %s%n", sum);
        }

        public static void solvingSumOfLengthsUnfold() {
            var sum = SpringService.getSumOfPossibleUnFoldComboRecursion("12-Springs");
            System.out.printf("Solving Day 12 Sum Of Counts: %s%n", sum);
        }
}
