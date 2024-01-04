package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PulseServiceTest {

    @Test
    void shouldCountPulse() {
        assertEquals(32000000, PulseService.getPulseProduct("20-input-test"));
        assertEquals(11687500, PulseService.getPulseProduct("20-input-test-2"));
    }

    @Test
    void shouldGetValueFromClass() {
        var modules = PulseService.getModules("20-input-test");

        assertEquals(5, modules.size());
        assertEquals("[inv, a, b, c, broadcaster]", modules.keySet().toString());
    }
}
