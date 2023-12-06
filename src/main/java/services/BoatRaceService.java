package services;

import java.util.Arrays;
import java.util.List;

public class BoatRaceService {

    public static final String SPACES = "\\s+";
    public static final String LINE_BREAK = "\n";
    public static final String COLON = ":";
    public static final String EMPTY = "";

    static int getNumOfWayBeatRecord(final long totalTime, final long distance) {
        var count = 0;
        for(var buttonPressedTime = 1; buttonPressedTime < totalTime; buttonPressedTime++) {
            if (buttonPressedTime * (totalTime - buttonPressedTime) > distance) {
                count++;
            }
        }
        return count;
    }

    static List<Long> getNumbers(final String fileName, final int lineIndex) {
        String[] s = FileService.readStringFromDocument(fileName).split(LINE_BREAK);
        return Arrays.stream(s[lineIndex].substring(s[lineIndex].indexOf(COLON) + 1)
                        .trim()
                        .split(SPACES))
                .map(Long::parseLong)
                .toList();
    }

    public static long getMultiplyOfNumOfWay(final String fileName) {
        var timeList = getNumbers(fileName, 0);
        var distanceList = getNumbers(fileName, 1);

        long multiple = 1;
        for(var i = 0; i < timeList.size(); i++) {
            multiple *= getNumOfWayBeatRecord(timeList.get(i), distanceList.get(i));
        }

        return multiple;
    }

    static long getNumber(final String fileName, final int lineIndex) {
        String[] s = FileService.readStringFromDocument(fileName).split(LINE_BREAK);
        return Long.parseLong(s[lineIndex].substring(s[lineIndex].indexOf(COLON) + 1)
                .trim()
                .replaceAll(SPACES, EMPTY));
    }

    public static long getNumOfWay(final String fileName) {
        return getNumOfWayBeatRecord(
                getNumber(fileName, 0),
                getNumber(fileName, 1));
    }
}
