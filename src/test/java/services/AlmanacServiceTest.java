package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlmanacServiceTest {
    @Test
    void getSeedToSoilMap() {
        var mapString = "50 98 2\n" + "52 50 48";
        var seedToSoilMap = new SourceDestinationList(mapString);

        assertEquals(2, seedToSoilMap.getSize());

        assertEquals(81, seedToSoilMap.getDestinationValue(79));
        assertEquals(14, seedToSoilMap.getDestinationValue(14));
        assertEquals(57, seedToSoilMap.getDestinationValue(55));
        assertEquals(13, seedToSoilMap.getDestinationValue(13));
        assertEquals(99, seedToSoilMap.getDestinationValue(97));
        assertEquals(50, seedToSoilMap.getDestinationValue(98));
        assertEquals(1000, seedToSoilMap.getDestinationValue(1000));
    }

    @Test
    void shouldGetSeeds() {
        assertEquals("[79, 14, 55, 13]", AlmanacService.getSeeds("5-Almanac-test").toString());
    }

    @Test
    void shouldGetSourceDestinationLists() {
        assertEquals(7, AlmanacService.getSourceDestinationLists("5-Almanac-test").size());
    }

    @Test
    void shouldGetLowestLocationNumber() {
        assertEquals(35, AlmanacService.getLowestLocationNumber("5-Almanac-test"));
    }
    @Test
    void test() {
        assertEquals(46, AlmanacService.getLowestLocationNumberWithRangeSeed("5-Almanac-test"));
    }
}