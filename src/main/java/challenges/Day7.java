package challenges;

import services.CamelCardsService;

public class Day7 {
    public static void solvingTotalWinnings() {
        var total = CamelCardsService.getTotalWinnings("7-Camel-Cards");
        System.out.printf("Solving Day 7 Total Winnings: %s%n", total);
    }
    public static void solvingTotalWinningsWithWildcards() {
        var total = CamelCardsService.getTotalWinningsWithWildcards("7-Camel-Cards");
        System.out.printf("Solving Day 7 Total Winnings With Wildcards: %s%n", total);
    }
}
