package services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileServiceTest {

    @ParameterizedTest()
    @CsvSource(quoteCharacter = '*', textBlock = """
            *1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet*, *1-CalibrationDocument-test*
            """)
    void shouldReadStringFromDocument(String expected, String fileName) {
        assertEquals(expected, FileService.readStringFromDocument(fileName));
    }

}