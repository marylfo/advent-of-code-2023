package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HolidayHashServiceTest {

    @Test
    void shouldGetHashValue() {
        assertEquals(52, Step.getHashValue("HASH"));
    }

    @Test
    void getSum() {
        assertEquals(1320, HolidayHashService.getSum("15-Sequence-test"));
    }

    @Test
    void test() {
        assertEquals(145, HolidayHashService.getFocusPower("15-Sequence-test"));
    }
}
