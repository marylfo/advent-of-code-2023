package services;

import java.util.Arrays;
import java.util.List;

public class ScratchcardsService {
    public static int getSumOfPoints(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(ScratchcardsService::getPoint)
                .reduce(0, Integer::sum);
    }

    public static int getNumOfScratchcards(String fileName) {
        var totalCardNumber = (int) FileService.readStringFromDocument(fileName).lines().count();
        int[] cardCount = new int[totalCardNumber];
        Arrays.fill(cardCount, 1);

        var matchNumbersOfCard = FileService.readStringFromDocument(fileName)
                .lines()
                .map(ScratchcardsService::getMatchingNumber)
                .toList();

        for (int i = 0; i < totalCardNumber; i++) {
            var matchNumber = matchNumbersOfCard.get(i);
            if (matchNumber > 0) {
                var nextCardIndex = i + 1;
                while(nextCardIndex < totalCardNumber && matchNumber > 0) {
                    cardCount[nextCardIndex] += cardCount[i];
                    nextCardIndex++;
                    matchNumber--;
                }
            }
        }

        return Arrays.stream(cardCount).reduce(0, Integer::sum);
    }

    static int getPoint(String line) {
        var winningNumbers = getNumbersList(line, line.indexOf(":") + 1, line.indexOf("|"));
        var numbers = getNumbersList(line, line.indexOf("|") + 1, line.length());

        var numOfHavingWinningNumber = numbers.stream()
                .filter(winningNumbers::contains)
                .count();

        return numOfHavingWinningNumber > 0 ?
                (int) Math.pow(2, numOfHavingWinningNumber - 1) :
                0;
    }

    static int getMatchingNumber(String line) {
        var winningNumbers = getNumbersList(line, line.indexOf(":") + 1, line.indexOf("|"));
        var numbers = getNumbersList(line, line.indexOf("|") + 1, line.length());

        return (int) numbers.stream()
                .filter(winningNumbers::contains)
                .count();
    }

    private static List<Integer> getNumbersList(String line, int beginIndex, int endIndex) {
        return Arrays.stream(line.substring(beginIndex, endIndex)
                        .trim()
                        .split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }
}
