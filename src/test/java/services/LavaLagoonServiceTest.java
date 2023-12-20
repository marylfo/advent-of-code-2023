package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LavaLagoonServiceTest {

    @Test
    void shouldGetCubicMeters() {
        assertEquals(62, LavaLagoonService.getCubicMeters("18-Plan-test"));
    }
    @Test
    void shouldGetCubicMetersUsingColor() {
        assertEquals(952408144115L, LavaLagoonService.getCubicMetersUsingColor("18-Plan-test"));
    }
}
