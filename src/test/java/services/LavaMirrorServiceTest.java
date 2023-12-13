package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LavaMirrorServiceTest {
    @Test
    void shouldGetNumberFromVerticalAxis() {
        String patternStr = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.
                """;

        var pattern = new Pattern(patternStr);
        assertTrue(pattern.isColumnEqual(5, 6));
        assertEquals(5, pattern.getVerticalAxis());
        assertEquals(5, pattern.getNumber());
    }

    @Test
    void shouldGetColumnNumberEdgeCase() {
        String patternStr = """
                ###.#.##.#.##
                ..##.####.##.
                ##...#..#...#
                .....#..#....
                ####.####.#.#
                ..#.#.##.#.#.
                #####....####
                ..####..####.
                ..##########.
                ##..##..##..#
                ...#......#..
                ...#.####.#..
                ######..#####
                ###.######.##
                ##..##..##..#
                """;

        var pattern = new Pattern(patternStr);
        assertTrue(pattern.isColumnEqual(1, 2));
        assertEquals(1, pattern.getVerticalAxis());
        assertEquals(1, pattern.getNumber());
    }

    @Test
    void shouldGetNumberFromHorizontalAxis() {
        String patternStr = """
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
                """;

        var pattern = new Pattern(patternStr);
        assertTrue(pattern.isRowEqual(4, 5));
        assertEquals(4, pattern.getHorizontalAxis());
        assertEquals(400, pattern.getNumber());
    }

    @Test
    void shouldGetSumOfNotes() {
        assertEquals(405, LavaMirrorService.getSumOfNotes("13-Lava-test"));
    }

    @Test
    void shouldGetIsRowDifferentWithOneSymbol() {
        String patternStr = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.
                """;

        var pattern = new Pattern(patternStr);
        assertTrue(pattern.isRowDifferentWithOneSymbol(1, 6));
    }

    @Test
    void shouldGetIsRowDifferentWithOneSymbol2() {
        String patternStr = """
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
                """;

        var pattern = new Pattern(patternStr);
        assertTrue(pattern.isRowDifferentWithOneSymbol(1, 2));
    }

    @Test
    void shouldGetIsColDifferentWithOneSymbol() {
        String patternStr = """
                .##.
                ..#.
                .##.
                """;

        var pattern = new Pattern(patternStr);
        assertTrue(pattern.isColumnDifferentWithOneSymbol(2, 3));
    }

    @Test
    void shouldGetNumberWithSmudge() {
        String patternStr = """
                ..####.....
                ##....####.
                ..#..#....#
                #.####.##..
                #.####.##..
                #.####.#.##
                .#....#...#
                #......#.##
                #.####.##..
                ..####..##.
                ..#..#..#.#
                .#.##.#.##.
                .#.##.#.##.
                ..#..#..###
                ..####..##.
                """;

        var pattern = new Pattern(patternStr);
        assertEquals(1200, pattern.getNumberWithSmudge());
    }

    @Test
    void shouldGetSumOfNotesWithSmudge() {
        assertEquals(400 , LavaMirrorService.getSumOfNotesWithSmudge("13-Lava-test"));
    }
    @Test
    void shouldGetSumOfNotesWithSmudge2() {
        assertEquals(1400 , LavaMirrorService.getSumOfNotesWithSmudge("13-Lava-test-2"));
    }
}
