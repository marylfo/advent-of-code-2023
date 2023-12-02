package services;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CalibrationValueRecoveryService {

    public static final int DIGIT_LETTER_MIN_LENGTH = 3;
    public static final String[] DIGIT_WORDS = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    public static final String NON_DIGIT_REGEX = "\\D";
    public static final String IS_DIGIT_REGEX = "\\d";
    public static final String SPLIT_BY_DIGIT_AND_KEEP_DIGIT_REGEX = "((?=\\d)|(?<=\\d))";

    public static Integer produceValueFromDocument(String fileName) {
        return FileService
                .readStringFromDocument(fileName)
                .lines()
                .map(CalibrationValueRecoveryService::getCalibrationValueFromOneLine)
                .reduce(0, Integer::sum);
    }
    public static Integer produceValueFromDocumentWithLetterSpelled(String fileName) {
        return FileService
                .readStringFromDocument(fileName)
                .lines()
                .map(CalibrationValueRecoveryService::getCalibrationValueFromOneLineWithLetterSpelled)
                .reduce(0, Integer::sum);
    }

    static Integer getCalibrationValueFromOneLine(String str) {
        List<Integer> digits = Arrays.stream(str.replaceAll(NON_DIGIT_REGEX, "")
                .split(""))
                .map(Integer::parseInt)
                .toList();

        return digits.get(0) * 10 + digits.get(digits.size()-1);
    }

    static Integer getCalibrationValueFromOneLineWithLetterSpelled(String str) {
        List<Integer> digits = Arrays.stream(processDigitWordsInLine(str).split(""))
                .map(Integer::parseInt)
                .toList();

        return digits.get(0) * 10 + digits.get(digits.size()-1);
    }

    static String processDigitWordsInLine(String str) {
        return Arrays.stream(str.split(SPLIT_BY_DIGIT_AND_KEEP_DIGIT_REGEX))
                .map(CalibrationValueRecoveryService::convertWordsIntoDigits)
                .reduce("", String::concat);
    }

    static String convertWordsIntoDigits(String str) {
        if (Pattern.compile(IS_DIGIT_REGEX).matcher(str).matches()) {
            return str;
        }

        StringBuilder convertedString = new StringBuilder();

        int i = 0, j = i + DIGIT_LETTER_MIN_LENGTH;
        int strLength = str.length();

        while (i < strLength - 1) {
            while (j <= strLength) {
                for (int idx = 0; idx < DIGIT_WORDS.length; idx++) {
                    if (str.substring(i, j).compareTo(DIGIT_WORDS[idx]) == 0) {
                        convertedString.append(idx+1);
                        // digit words can share the same char as the ending char and starting char
                        i = j - 1;
                    }
                }
                j++;
            }

            if ((j == strLength + 1) && (i < strLength - DIGIT_LETTER_MIN_LENGTH)) {
                // Move the substring header to next position if the checking reach end
                i++;
                j = i + DIGIT_LETTER_MIN_LENGTH;
            } else {
                // Exit the checking
                return convertedString.toString();
            }
        }
        return convertedString.toString();
    }
}
