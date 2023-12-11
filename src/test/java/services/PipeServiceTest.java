package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PipeServiceTest {
    @Test
    void shouldGetStepsToFarthestPoint() {
        assertEquals(4, PipeService.getStepsToFarthestPoint("10-Grid-test"));
        assertEquals(8, PipeService.getStepsToFarthestPoint("10-Grid-test-2"));
    }

    @Test
    void shouldGetNumberOfEnclosedTiles() {
        assertEquals(1, PipeService.getNumberOfEnclosedTiles("10-Grid-test"));
        assertEquals(4, PipeService.getNumberOfEnclosedTiles("10-Grid-test-3"));
        assertEquals(8, PipeService.getNumberOfEnclosedTiles("10-Grid-test-4"));
        assertEquals(10, PipeService.getNumberOfEnclosedTiles("10-Grid-test-5"));
    }
}
