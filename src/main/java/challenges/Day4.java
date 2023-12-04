package challenges;


import services.ScratchcardsService;

public class Day4 {
    public static void solvingScratchcardsPoints() {
        Integer sum = ScratchcardsService.getSumOfPoints("4-Scratchcards");
        System.out.printf("Solving Day 4 Scratchcards Points: %s%n", sum);
    }
    public static void solvingTotalNumberOfScratchcards() {
        Integer sum = ScratchcardsService.getNumOfScratchcards("4-Scratchcards");
        System.out.printf("Solving Day 4 Scratchcards Number: %s%n", sum);
    }
}
