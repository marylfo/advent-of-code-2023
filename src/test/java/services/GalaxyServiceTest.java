package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GalaxyServiceTest {
    @Test
    void getSumOfLengths() {
        assertEquals(374, GalaxyService.getSumOfLengths("11-Galaxy-test", 1));
    }

    @Test
    void getSumOfLengths_10() {
        assertEquals(1030, GalaxyService.getSumOfLengths("11-Galaxy-test", 10));
    }
    @Test
    void getSumOfLengths_100() {
        assertEquals(8410, GalaxyService.getSumOfLengths("11-Galaxy-test", 100));
    }
}
