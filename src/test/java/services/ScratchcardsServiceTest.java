package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScratchcardsServiceTest {

    @Test
    void shouldGetSumOfPoints() {
        assertEquals(13, ScratchcardsService.getSumOfPoints("4-Scratchcards-test"));
    }

    @Test
    void shouldGetPoint() {
        assertEquals(8, ScratchcardsService.getPoint("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"));
    }

    @Test
    void shouldGetNumOfScratchcards() {
        assertEquals(30, ScratchcardsService.getNumOfScratchcards("4-Scratchcards-test"));
    }

    @Test
    void shouldGetMatchingNumber() {
        assertEquals(4, ScratchcardsService.getMatchingNumber("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"));
    }
}
