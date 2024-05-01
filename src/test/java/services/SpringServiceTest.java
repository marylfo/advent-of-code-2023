package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpringServiceTest {
    @Test
    void shouldGetPossiblePatterns() {
        var spring = new Spring("???.### 1,1,3");
        assertEquals(1, spring.getPossiblePatterns());

        var spring2 = new Spring(".??..??...?##. 1,1,3");
        assertEquals(4, spring2.getPossiblePatterns());
    }

    @Test
    void shouldGetSumOfPossibleCombo() {
        assertEquals(21, SpringService.getSumOfPossibleCombo("12-Springs-test"));
    }

    @Test
    void shouldGetSumOfPossibleComboRecursion() {
        assertEquals(21, SpringService.getSumOfPossibleComboRecursion("12-Springs-test"));
    }

    @Test
    void shouldGetSumOfPossibleUnfoldCombo() {
        assertEquals(525152, SpringService.getSumOfPossibleUnFoldComboRecursion("12-Springs-test"));
    }
}