package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

public class OasisReportService {
    static boolean isEndOfList(List<Long> list) {
        for (var value: list) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

    static List<Long> getDifference(List<Long> list) {
        return LongStream.range(1, list.size())
                .mapToObj(i -> list.get((int) i) - list.get((int) i - 1))
                .toList();
    }

    private static List<List<Long>> getDifferences(List<Long> history) {
        List<List<Long>> differences = new ArrayList<>();
        var currentList = history;

        while (!isEndOfList(currentList)) {
            differences.add(currentList);
            currentList = getDifference(currentList);
        }

        return differences;
    }

    static long getValueAtEnd(final List<Long> history) {
        return getDifferences(history).stream()
                .map(difference -> difference.get(difference.size() - 1))
                .reduce(0L, Long::sum);
    }

    static long getValueAtStart(final List<Long> history) {
        var differences = getDifferences(history);

        return LongStream.range(0, differences.size())
                .mapToObj(index -> {
                    long sign = (index % 2 == 0) ? 1 : -1;
                    return sign * differences.get((int) index).get(0);
                })
                .reduce(0L, Long::sum);
    }

    public static long getSumOfExtrapolatedValuesAtEnd(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(line -> Arrays.stream(line.split(" "))
                            .map(Long::parseLong).toList())
                .map(OasisReportService::getValueAtEnd)
                .reduce(0L, Long::sum);
    }

    public static long getSumOfExtrapolatedValuesAtStart(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(line -> Arrays.stream(line.split(" "))
                        .map(Long::parseLong).toList())
                .map(OasisReportService::getValueAtStart)
                .reduce(0L, Long::sum);
    }
}
