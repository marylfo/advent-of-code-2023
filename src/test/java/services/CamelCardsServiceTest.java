package services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CamelCardsServiceTest {

    @ParameterizedTest
    @CsvSource({"7, AAAAA", "6, AA8AA", "5, 23332", "4, TTT98", "3, 23432", "2, A23A4", "1, 23456"})
    void shouldGetHandRank(int expected, String handString) {
        var hand = new Hand(handString, "0");
        assertEquals(expected, hand.getRank());
    }

    @ParameterizedTest
    @CsvSource({"14, A", "13, K", "12, Q", "11, J", "10, T", "2, 2"})
    void shouldGetCardRank(int expected, String label) {
        var card = new Card(label);
        assertEquals(expected, card.getRank());
    }

    @ParameterizedTest
    @CsvSource({
            "1, AAAAA, AAA88", "0, AAAAA, AAAAA",
            "1, 33332, 2AAAA", "-1, 2AAAA, 33332",
    })
    void shouldGetCompareCard(int expected, String hand1Str, String hand2Str) {
        var hand1 = new Hand(hand1Str, "0");
        var hand2 = new Hand(hand2Str, "0");

        assertEquals(expected, hand1.compareTo(hand2));
    }

    @Test
    void shouldGetTotalWinnings() {
        assertEquals(6440, CamelCardsService.getTotalWinnings("7-Camel-Cards-test"));
    }

    @ParameterizedTest
    @CsvSource({"7, AAAAJ", "6, AAAJ9", "5, 2J332", "4, TTJ98", "3, 23432", "2, J23A4", "1, 23456"})
    void shouldGetHandRanding(int expected, String handString) {
        var hand = new HandWithWildCard(handString, "0");
        assertEquals(expected, hand.getRank());
    }

    @ParameterizedTest
    @CsvSource({"14, A", "13, K", "12, Q", "1, J", "10, T", "2, 2"})
    void shouldGetCardWithWildCardsRank(int expected, String label) {
        var card = new CardWithWildCards(label);
        assertEquals(expected, card.getRank());
    }

    @ParameterizedTest
    @CsvSource({
            "1, AAAAJ, AAA88", "0, JJJJA, JJJJA",
            "1, 33332, 2AAAA", "-1, JJJ12, 33332",
    })
    void shouldGetCompareHandWIthWildCard(int expected, String hand1Str, String hand2Str) {
        var hand1 = new HandWithWildCard(hand1Str, "0");
        var hand2 = new HandWithWildCard(hand2Str, "0");

        assertEquals(expected, hand1.compareTo(hand2));
    }

    @Test
    void shouldGetTotalWinningsWithWildCard() {
        assertEquals(5905, CamelCardsService.getTotalWinningsWithWildcards("7-Camel-Cards-test"));
    }
}
