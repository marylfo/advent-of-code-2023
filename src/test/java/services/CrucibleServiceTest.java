package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrucibleServiceTest {
    @Test
    public void testPart1() {
        var service = new CrucibleService("17-Map-test");
        assertEquals(102, service.getMinHeatLoss());
    }

    @Test
    public void testPart2() {
        var service = new CrucibleService("17-Map-test");
        assertEquals(94, service.getMinHeatLossUltra());
    }
}