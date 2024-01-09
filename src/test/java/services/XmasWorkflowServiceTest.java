package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmasWorkflowServiceTest {

    @Test
    void shouldCreatePartObject() {
        Part p = new Part("{x=787,m=2655,a=1222,s=2876}");
        assertEquals("787 2655 1222 2876", p.toString());
    }

    @Test
    void shouldGetSumOfRatingNumbers() {
        assertEquals(19114, XmasWorkflowService.getSumOfRatingNumbers("19-puzzle-test"));
    }

    @Test
    void shouldCalculateDistinctCombo() {
        Range r = new Range();
        assertEquals(Math.pow(4000,4) , r.getDistinctCombo());
    }

    @Test
    void shouldCalculateDistinctComboFromFile() {
        assertEquals(167409079868000L, XmasWorkflowService.getDistinctCombo("19-puzzle-test"));
    }

}
