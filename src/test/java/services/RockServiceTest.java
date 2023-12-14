package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RockServiceTest {

    @Test
    void shouldSlideRocksToNorth() {
       assertEquals(".#O..#O...", RockMap.slideRocksToLeft(".#.O.#O..."));
    }

    @Test
    void shouldGetTotalLoad() {
        assertEquals(136, RockService.getTotalLoad("14-Rock-test"));
    }

    @Test
    void shouldGetTotalLoadFromCycle() {
        assertEquals(64, RockService.getTotalLoadFromCycle("14-Rock-test"));
    }
}
