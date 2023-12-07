package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Card implements Comparable<Card> {
    private final String label;
    Card(String label) {
        this.label = label;
    }

    int getRank() {
        return switch (label) {
            case "A" -> 14;
            case "K" -> 13;
            case "Q" -> 12;
            case "J" -> 11;
            case "T" -> 10;
            default -> Integer.parseInt(label);
        };
    }

    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.getRank(), other.getRank());
    }
}

class Hand implements Comparable<Hand> {
    private final String hand;
    private final int bid;
    private final int maxCount;
    private final int numOfDistinctLabels;

    Hand(String hand, String bid) {
        HashMap<String, Integer> charCount = new HashMap<>();
        for (String c : hand.split("")) {
            if (charCount.containsKey(c)) {
                charCount.put(c, charCount.get(c) + 1);
            } else {
                charCount.put(c, 1);
            }
        }

        this.hand = hand;
        this.bid = Integer.parseInt(bid);
        this.maxCount = charCount.values().stream().max(Integer::compareTo).orElse(0);
        this.numOfDistinctLabels = charCount.keySet().size();
    }

    //7 Five of a kind, where all five cards have the same label: AAAAA
    //6 Four of a kind, where four cards have the same label and one card has a different label: AA8AA
    //5 Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
    //4 Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
    //3 Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
    //2 One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
    //1 High card, where all cards' labels are distinct: 23456
    static int getHandRank(int maxCount, int numOfDistinctLabels) {
        if (maxCount == 5) {
            return 7;
        } else if (maxCount == 4) {
            return 6;
        } else if (maxCount == 3 && numOfDistinctLabels == 2) {
            return 5;
        } else if (maxCount == 3 && numOfDistinctLabels == 3) {
            return 4;
        } else if (maxCount == 2 && numOfDistinctLabels == 3) {
            return 3;
        } else if (maxCount == 2 && numOfDistinctLabels == 4) {
            return 2;
        } else {
            return 1;
        }
    }

    int getRank() {
        return getHandRank(maxCount, numOfDistinctLabels);
    }

    String getCardAtIndex(int index) {
        return hand.substring(index, index + 1);
    }

    public int getBid() {
        return bid;
    }

    @Override
    public int compareTo(Hand otherHand) {
        if (this.getRank() > otherHand.getRank()) {
            return 1;
        } else if (this.getRank() < otherHand.getRank()) {
            return -1;
        } else {
            for(var i = 0; i < 5; i++) {
                var card = new Card(this.getCardAtIndex(i));
                var otherHandCard = new Card(otherHand.getCardAtIndex(i));

                var cardRank = card.compareTo(otherHandCard);
                if (cardRank != 0) {
                    return cardRank;
                }
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        return "%s (%s)".formatted(hand, bid);
    }
}

public class CamelCardsService {
    public static long getTotalWinnings(String fileName) {
        List<Hand> hands = new ArrayList<>();

        FileService.readStringFromDocument(fileName)
                .lines()
                .forEach(line -> {
                    var lineSplit = line.split(" ");
                    hands.add(new Hand(lineSplit[0], lineSplit[1]));
                });

        Collections.sort(hands);

        long total = 0;
        for (var i = 0; i < hands.size(); i++) {
            total += (long) (i + 1) * hands.get(i).getBid();
        }

        return total;
    }

    public static long getTotalWinningsWithWildcards(String fileName) {
        List<HandWithWildCard> hands = new ArrayList<>();

        FileService.readStringFromDocument(fileName)
                .lines()
                .forEach(line -> {
                    var lineSplit = line.split(" ");
                    hands.add(new HandWithWildCard(lineSplit[0], lineSplit[1]));
                });

        Collections.sort(hands);

        long total = 0;
        for (var i = 0; i < hands.size(); i++) {
            total += (long) (i + 1) * hands.get(i).getBid();
        }

        return total;
    }
}


class CardWithWildCards implements Comparable<CardWithWildCards> {
    private final String label;
    CardWithWildCards(String label) {
        this.label = label;
    }

    int getRank() {
        return switch (label) {
            case "A" -> 14;
            case "K" -> 13;
            case "Q" -> 12;
            case "J" -> 1;
            case "T" -> 10;
            default -> Integer.parseInt(label);
        };
    }

    @Override
    public int compareTo(CardWithWildCards other) {
        return Integer.compare(this.getRank(), other.getRank());
    }
}


class HandWithWildCard implements Comparable<HandWithWildCard> {
    private final String hand;
    private final int bid;
    private final int maxCount;
    private final int numOfDistinctLabels;

    HandWithWildCard(String hand, String bid) {
        HashMap<String, Integer> charCount = new HashMap<>();
        for (String c : hand.split("")) {
            if (charCount.containsKey(c)) {
                charCount.put(c, charCount.get(c) + 1);
            } else {
                charCount.put(c, 1);
            }
        }

        if (charCount.containsKey("J") && charCount.get("J") < 5) {
            var numOfJ = charCount.remove("J");
            var max = charCount.values().stream().max(Integer::compareTo).orElse(0);
            for(Map.Entry<String, Integer> entry : charCount.entrySet()) {
                if (entry.getValue().equals(max)) {
                    charCount.put(entry.getKey(), max + numOfJ);
                }
            }
        }

        this.hand = hand;
        this.bid = Integer.parseInt(bid);
        this.maxCount = charCount.values().stream().max(Integer::compareTo).orElse(0);
        this.numOfDistinctLabels = charCount.keySet().size();
    }

    int getRank() {
        return Hand.getHandRank(maxCount, numOfDistinctLabels);
    }

    String getCardAtIndex(int index) {
        return hand.substring(index, index + 1);
    }

    public int getBid() {
        return bid;
    }

    @Override
    public int compareTo(HandWithWildCard otherHand) {
        if (this.getRank() > otherHand.getRank()) {
            return 1;
        } else if (this.getRank() < otherHand.getRank()) {
            return -1;
        } else {
            for(var i = 0; i < 5; i++) {
                var card = new CardWithWildCards(this.getCardAtIndex(i));
                var otherHandCard = new CardWithWildCards(otherHand.getCardAtIndex(i));

                var cardRank = card.compareTo(otherHandCard);
                if (cardRank != 0) {
                    return cardRank;
                }
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        return "%s (%s)".formatted(hand, bid);
    }
}