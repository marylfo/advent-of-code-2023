package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapNetworkServiceTest {
    @Test
    void shouldGetNumOfSteps() {
        assertEquals(2, MapNetworkService.getNumOfSteps("8-Map-test1"));
    }
    @Test
    void shouldGetNumOfSteps2() {
        assertEquals(6, MapNetworkService.getNumOfSteps("8-Map-test2"));
    }

    @Test
    void shouldGtNumOfSimultaneouslySteps() {
        assertEquals(6, MapNetworkService.getNumOfSimultaneouslySteps("8-Map-test3"));
    }
}
