package services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoatRaceServiceTest {

    public static final String TEST_FILE_NAME = "6-Race-test";

    @ParameterizedTest
    @CsvSource({"4, 7, 9", "8, 15, 40", "9, 30, 200"})
    void shouldGetNumOfWayBeatRecord(long expected, long time, long distance) {
        assertEquals(expected, BoatRaceService.getNumOfWayBeatRecord(time,distance));
    }

    @Test
    void shouldGetMultiplyOfNumOfWay() {
        assertEquals(288, BoatRaceService.getMultiplyOfNumOfWay(TEST_FILE_NAME));
    }

    @Test
    void shouldGetNumOfWay() {
        assertEquals(71503, BoatRaceService.getNumOfWay(TEST_FILE_NAME));
    }
}
