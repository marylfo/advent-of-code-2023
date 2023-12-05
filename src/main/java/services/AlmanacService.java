package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SourceDestinationRecord implements Comparable<SourceDestinationRecord> {
    private final long destinationRangeStart;
    private final long sourceRangeStart;
    private final long rangeLength;

    SourceDestinationRecord(long destinationRangeStart, long sourceRangeStart, long rangeLength) {
        this.destinationRangeStart = destinationRangeStart;
        this.sourceRangeStart = sourceRangeStart;
        this.rangeLength = rangeLength;
    }

    public long getSourceRangeStart() {
        return sourceRangeStart;
    }

    public long getSourceRangeEnd() {
        return sourceRangeStart + rangeLength - 1;
    }

    public long getDestinationRangeStart() {
        return destinationRangeStart;
    }

    @Override
    public int compareTo(SourceDestinationRecord otherRecord) {
        return Long.compare(this.sourceRangeStart, otherRecord.sourceRangeStart);
    }

    @Override
    public String toString() {
        return "sourceRangeStart: %d\tdestinationRangeStart: %d\trangeLength: %d\n"
                .formatted(this.sourceRangeStart, this.destinationRangeStart, this.rangeLength);
    }
}

class SourceDestinationList {
    public static final String SPACE = " ";
    private final List<SourceDestinationRecord> list;

    SourceDestinationList(String inputRecord) {
        list = new ArrayList<>();

        inputRecord.lines().forEach(line -> {
            var values = line.trim().split(SPACE);

            list.add(new SourceDestinationRecord(
                    Long.parseLong(values[0]),
                    Long.parseLong(values[1]),
                    Long.parseLong(values[2])
            ));
        });

        Collections.sort(list);
    }

    public int getSize() {
        return list.size();
    }

    public long getDestinationValue(long sourceValue) {
        for (var record: list) {
            if (isWithinRangeInRecord(sourceValue, record)) {
                return record.getDestinationRangeStart() + (sourceValue - record.getSourceRangeStart());
            }
        }

        return sourceValue;
    }

    private static boolean isWithinRangeInRecord(
            long sourceValue,
            SourceDestinationRecord record
    ) {
        return sourceValue >= record.getSourceRangeStart() &&
                sourceValue <= record.getSourceRangeEnd();
    }
}

public class AlmanacService {
    public static final String EMPTY_LINE = "\\n\\n";
    public static final String COLON_SYMBOL = ":";
    public static final String SPACE = " ";

    static List<Long> getSeeds(String fileName) {
        var almanacLines = FileService.readStringFromDocument(fileName).split(EMPTY_LINE);

        return Arrays.stream(almanacLines[0]
                        .substring(almanacLines[0].indexOf(COLON_SYMBOL) + 1)
                        .trim()
                        .split(SPACE)
                ).map(Long::parseLong).toList();
    }

    static List<SourceDestinationList> getSourceDestinationLists(String fileName) {
        var almanacLines = FileService.readStringFromDocument(fileName).split(EMPTY_LINE);

        List<SourceDestinationList> pipelineList = new ArrayList<>();

        for (int i = 1; i < almanacLines.length; i++) {
            pipelineList.add(new SourceDestinationList(
                    almanacLines[i].substring(almanacLines[i].indexOf(COLON_SYMBOL) + 2)
            ));
        }

        return pipelineList;
    }

    private static Long getLocationNumber(
            Long seed,
            List<SourceDestinationList> pipelineLists
    ) {
        var value = seed;
        for (var list : pipelineLists) {
            value = list.getDestinationValue(value);
        }
        return value;
    }

    public static long getLowestLocationNumber(String fileName) {
        var seeds = getSeeds(fileName);
        var pipelineLists = getSourceDestinationLists(fileName);

        return seeds.stream()
                .map(seed -> getLocationNumber(seed, pipelineLists))
                .min(Long::compare).orElse(0L);
    }

    public static long getLowestLocationNumberWithRangeSeed(String fileName) {
        long lowest = Long.MAX_VALUE;

        var seedsAndRanges = getSeeds(fileName);
        var pipelineLists = getSourceDestinationLists(fileName);
        for (var i = 0; i < seedsAndRanges.size(); i+=2) {
            var startValue = seedsAndRanges.get(i);
            var increament = 0;
            while (increament < seedsAndRanges.get(i+1)) {
                var location = getLocationNumber(startValue + increament, pipelineLists);
                if (location < lowest) {
                    lowest = location;
                }
                increament++;
            }
        }

        return lowest;
    }
}
