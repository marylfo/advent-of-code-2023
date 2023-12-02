package services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalibrationValueRecoveryServiceTest {

    @ParameterizedTest()
    @CsvSource({"1abc2, 12", "pqr3stu8vwx, 38", "a1b2c3d4e5f, 15", "treb7uchet, 77"})
    void shouldGetCalibrationValueFromOneLine(String input, int result) {
        assertEquals(result, CalibrationValueRecoveryService.getCalibrationValueFromOneLine(input));
    }

    @ParameterizedTest()
    @CsvSource(quoteCharacter = '*', textBlock = """
            *1-CalibrationDocument-test*, 142
            """)
    void shouldProduceCalibrationValueFromDocument(String input, int result) {
        assertEquals(result, CalibrationValueRecoveryService.produceValueFromDocument(input));
    }

    @ParameterizedTest()
    @CsvSource(quoteCharacter = '*', textBlock = """
            *1-CalibrationDocumentWithLetterSpelled-test*, 281
            """)
    void shouldProduceCalibrationValueFromDocumentWithLetterSpelled(String input, int result) {
        assertEquals(result, CalibrationValueRecoveryService.produceValueFromDocumentWithLetterSpelled(input));
    }

    @ParameterizedTest()
    @CsvSource({"fouronevzkbnzm6seven47,47",
            "zphgdcznqsm2,22",
            "4gjnmxtrbflgp71,41",
            "4sqvv1cnpn,41",
            "8sevengzfvjrhnsb6ddb8ninerkgkxthtfkvbcmqs,89",
            "1seven336,16",
            "86one34vvvgdngbt39,89",
            "7oneight, 78"})
    void shouldGetCalibrationValueFromOneLineWithLetterSpelled(String input, int result) {
        assertEquals(result, CalibrationValueRecoveryService.getCalibrationValueFromOneLineWithLetterSpelled(input));
    }

    @ParameterizedTest()
    @CsvSource({"two1nine,219",
            "eightwothree,823",
            "abcone2threexyz,123",
            "xtwone3four,2134",
            "4nineeightseven2,49872",
            "zoneight234,18234",
            "7pqrstsixteen,76",
            "7oneight, 718"})
    void shouldConvertWordsIntoDigit(String input, String result) {
        assertEquals(result, CalibrationValueRecoveryService.processDigitWordsInLine(input));
    }
}
