package services;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OasisReportTest {
    private static ArrayList<Long> getTestList() {
        var test = new ArrayList<Long>();
        test.add(0L);
        test.add(3L);
        test.add(6L);
        test.add(9L);
        test.add(12L);
        test.add(15L);
        return test;
    }

    @Test
    void shouldGetIsEndOfList() {
        var test = getTestList();

        var difference = OasisReportService.getDifference(test);
        var difference2 = OasisReportService.getDifference(difference);
        assertTrue(OasisReportService.isEndOfList(difference2));
    }

    @Test
    void shouldGetDifference() {
        assertEquals("[3, 3, 3, 3, 3]", OasisReportService.getDifference(getTestList()).toString());
    }

    @Test
    void shouldGetDifferencesAtEnd() {
        assertEquals(18, OasisReportService.getValueAtEnd(getTestList()));
    }

    @Test
    void shouldGetSum() {
        assertEquals(114, OasisReportService.getSumOfExtrapolatedValuesAtEnd("9-Report-test"));
    }

    @Test
    void shouldGetDifferencesAtStart() {
        var test = new ArrayList<Long>();
        test.add(10L);
        test.add(13L);
        test.add(16L);
        test.add(21L);
        test.add(30L);
        test.add(45L);

        assertEquals(5, OasisReportService.getValueAtStart(test));
    }

    @Test
    void shouldGetSumAtStart() {
        assertEquals(2, OasisReportService.getSumOfExtrapolatedValuesAtStart("9-Report-test"));
    }
}
