package services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EngineSchematicServiceTest {

    public static final List<String> ENGINE_SCHEMATIC = EngineSchematicService.getEngineSchematicData(
            "3-EngineSchematic-test"
    );
    public static final int ENGINE_SCHEMATIC_LINE_LENGTH = 10;
    public static final int ENGINE_SCHEMATIC_SIZE = 10;

    @Test
    void shouldGetEngineSchematicData() {
        assertEquals(
                "[467..114.., ...*......, ..35..633., ......#..., 617*......, .....+.58., ..592....., ......755., ...$.*...., .664.598..]",
                EngineSchematicService.getEngineSchematicData("3-EngineSchematic-test").toString());
    }

    @Test
    void shouldGetLeftMostNumberBlock() {
        String leftmostNumberBlock = EngineSchematicService.getAdjacentCharactersOfNumber(
                0,
                0,
                3,
                ENGINE_SCHEMATIC,
                ENGINE_SCHEMATIC_SIZE,
                ENGINE_SCHEMATIC_LINE_LENGTH);

        assertEquals("467....*", leftmostNumberBlock);
    }
    @Test
    void shouldGet116NumberBlock() {
        String numberBlock = EngineSchematicService.getAdjacentCharactersOfNumber(
                0,
                5,
                3,
                ENGINE_SCHEMATIC,
                ENGINE_SCHEMATIC_SIZE,
                ENGINE_SCHEMATIC_LINE_LENGTH);

        assertEquals(".114......", numberBlock);

    }
    @Test
    void shouldGet35NumberBlock() {
        String numberBlock = EngineSchematicService.getAdjacentCharactersOfNumber(
                2,
                2,
                2,
                ENGINE_SCHEMATIC,
                ENGINE_SCHEMATIC_SIZE,
                ENGINE_SCHEMATIC_LINE_LENGTH);

        assertEquals("..*..35.....", numberBlock);
    }
    @Test
    void shouldGet664NumberBlock() {
        String numberBlock = EngineSchematicService.getAdjacentCharactersOfNumber(
                9,
                1,
                3,
                ENGINE_SCHEMATIC,
                ENGINE_SCHEMATIC_SIZE,
                ENGINE_SCHEMATIC_LINE_LENGTH);

        assertEquals("...$..664.", numberBlock);
    }

    @ParameterizedTest
    @CsvSource({"..*..35....., true, .....35....., false, ..+..35....., true, ..#..35....., true ,..$..35....., true"})
    void shouldGetIsPartNumber(String input, boolean result) {
        assertEquals(result, EngineSchematicService.isPartNumber(input));

    }


    @ParameterizedTest
    @CsvSource({"3-EngineSchematic-test, 4361"})
    void shouldGetSumOfPartNumbers(String fileName, int number) {
        assertEquals(number, EngineSchematicService.getSumOfPartNumbers(fileName));
    }

    @Test
    void shouldGetAsterisksLocation() {
        assertEquals(3, EngineSchematicService.getAsteriskLocations(ENGINE_SCHEMATIC).size());
    }

    @Test
    void shouldGetSumOfGearRations() {
        assertEquals(467835, EngineSchematicService.getSumOfGearRations("3-EngineSchematic-test"));
    }
}