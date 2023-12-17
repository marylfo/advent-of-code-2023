package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeamServiceTest {
    @Test
    void getNumberOfEnergizedTiles() {
        assertEquals(46, BeamService.getNumberOfEnergizedTiles("16-Layout-test"));
    }

    @Test
    void getLargestNumberOfEnergizedTiles() {
        assertEquals(51, BeamService.getLargestNumberOfEnergizedTiles("16-Layout-test"));
    }
}
