package services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CubeConundrumServiceTest {

    @ParameterizedTest()
    @CsvSource(quoteCharacter = '*', textBlock = """
            *Game 1: 4 blue, 7 red, 5 green; 3 blue, 4 red, 16 green; 3 red, 11 green*, 0
            """)
    void shouldGetGameIdIfGameIsPossible(String input, int result) {
        assertEquals(result, CubeConundrumService.getGameIdIfGameIsPossible(input));
    }
    @ParameterizedTest()
    @CsvSource(quoteCharacter = '*', textBlock = """
            *2-CubeGameData-test*, 8
            """)
    void shouldGetSumOfPossibleGameIds(String input, int result) {
        assertEquals(result, CubeConundrumService.getSumOfPossibleGameIds(input));
    }

    @ParameterizedTest()
    @CsvSource(quoteCharacter = '*', textBlock = """
            *Game 1: 4 blue, 7 red, 5 green; 3 blue, 4 red, 16 green; 3 red, 11 green*, 448
            """)
    void shouldGetSumOfPowerOfAGame(String input, int result) {
        assertEquals(result, CubeConundrumService.getSumOfPowerOfAGame(input));
    }
    @ParameterizedTest()
    @CsvSource(quoteCharacter = '*', textBlock = """
            *2-CubeGameData-test*, 2286
            """)
    void shouldGetSumOfPowerOfGames(String input, int result) {
        assertEquals(result, CubeConundrumService.getSumOfPowerOfGames(input));
    }
}