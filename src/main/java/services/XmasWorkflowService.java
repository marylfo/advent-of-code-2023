package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

record MinMax(Long min, Long max) {}
record RangeAndWorkflow (Range r, String w) {}

class Workflow {
    private String name;
    private String rules;

    Workflow(String input) {
        var pattern = Pattern.compile("(\\w+)\\{([\\w\\d<>:,]+)}");
        var matcher = pattern.matcher(input);

        if (matcher.find()) {
            name = matcher.group(1);
            rules = matcher.group(2);
        }
    }

    public String getName() {
        return name;
    }

    public String getRules() {
        return rules;
    }
}

class Part {
    private int x;
    private int m;
    private int a;
    private int s;

    Part(String input) {
        var pattern = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}");
        var matcher = pattern.matcher(input);

        if (matcher.find()) {
            x = Integer.parseInt(matcher.group(1));
            m = Integer.parseInt(matcher.group(2));
            a = Integer.parseInt(matcher.group(3));
            s = Integer.parseInt(matcher.group(4));
        }
    }

    boolean getResultFromRule(String rule) {
        var isLessThan = rule.charAt(1) == '<';
        var value = Integer.parseInt(rule.substring(2));

        return switch (rule.charAt(0)) {
            case 'x' -> isLessThan == (x < value);
            case 'm' -> isLessThan == (m < value);
            case 'a' -> isLessThan == (a < value);
            case 's' -> isLessThan == (s < value);
            default -> false;
        };
    }

    int getSum() {
        return x + m + a + s;
    }

    @Override
    public String toString() {
        return "%s %s %s %s".formatted(x, m ,a, s);
    }
}

class Range {
    public static final long RANGE_MIN = 1L;
    public static final long RANGE_MAX = 4000L;
    public static final char[] keys = {'x', 'm', 'a', 's'};
    private final HashMap<Character, MinMax> ranges;

    Range() {
        ranges = new HashMap<>();
        for (var key: keys) {
            ranges.put(key, new MinMax(RANGE_MIN, RANGE_MAX));
        }
    }

    Range(HashMap<Character, MinMax> ranges) {
        this.ranges = ranges;
    }

    Range getNewRange(String condition, boolean isGettingTrueRange) {
        var newR = new HashMap<Character, MinMax>();
        for (var key: keys) {
            newR.put(key, ranges.get(key));
        }

        var key = condition.charAt(0);
        var value = Long.parseLong(condition.substring(2));
        var rangeValue = newR.get(key);

        if (condition.contains("<") && isGettingTrueRange) {
            newR.put(key, new MinMax(rangeValue.min(), value - 1));
        } else if (condition.contains("<") && !isGettingTrueRange) {
            newR.put(key, new MinMax(value, rangeValue.max()));
        } else if (condition.contains(">") && isGettingTrueRange) {
            newR.put(key, new MinMax(value + 1, rangeValue.max()));
        } else if (condition.contains(">") && !isGettingTrueRange) {
            newR.put(key, new MinMax(rangeValue.min(), value));
        }

        return new Range(newR);
    }

    public long getDistinctCombo() {
        return ranges.values().stream()
                .map(range -> range.max() - range.min() + 1)
                .reduce(1L, (a, b) -> a * b);
    }

    List<RangeAndWorkflow> getNextRangeAndWorkflow(String rule) {
        var list = new ArrayList<RangeAndWorkflow>();

        if (rule.contains(":")) {
            var currRule = rule.substring(0, rule.indexOf(":"));

            var trueNext = rule.substring(rule.indexOf(":") + 1, rule.indexOf(","));
            var trueRange = this.getNewRange(currRule, true);
            if (!trueNext.contains(":")) {
                list.add(new RangeAndWorkflow(trueRange, trueNext));
            }

            var falseNext = rule.substring(rule.indexOf(",") + 1);
            var falseRange = this.getNewRange(currRule, false);

            while (falseNext.contains(":")) {
                currRule = falseNext.substring(0, falseNext.indexOf(":"));
                trueNext = falseNext.substring(falseNext.indexOf(":") + 1, falseNext.indexOf(","));
                trueRange = falseRange.getNewRange(currRule, true);

                if (!trueNext.contains(":")) {
                    list.add(new RangeAndWorkflow(trueRange, trueNext));
                }

                falseNext = falseNext.substring(falseNext.indexOf(",") + 1);
                falseRange = falseRange.getNewRange(currRule, false);
            }

            list.add(new RangeAndWorkflow(falseRange, falseNext));
        }

        return list;
    }
}

public class XmasWorkflowService {

    static int getResult(Part p, HashMap<String, Workflow> w) {
        var rules = w.get("in").getRules();

        var currRule = rules.substring(0, rules.indexOf(":"));

        var next = p.getResultFromRule(currRule) ?
                rules.substring(rules.indexOf(":") + 1, rules.indexOf(",")) :
                rules.substring(rules.indexOf(",") + 1);

        while (!next.contains(":")) {
            rules = w.get(next).getRules();
            currRule = rules.substring(0, rules.indexOf(":"));
            next = p.getResultFromRule(currRule) ?
                    rules.substring(rules.indexOf(":") + 1, rules.indexOf(",")) :
                    rules.substring(rules.indexOf(",") + 1);

            while (next.contains(":")) {
                currRule = next.substring(0, next.indexOf(":"));
                next = p.getResultFromRule(currRule) ?
                        next.substring(next.indexOf(":") + 1, next.indexOf(",")) :
                        next.substring(next.indexOf(",") + 1);
            }

            if (next.compareTo("A") == 0) {
                return p.getSum();
            }

            if (next.compareTo("R") == 0) {
                return 0;
            }
        }

        return 0;
    }

    public static int getSumOfRatingNumbers(String fileName) {
        HashMap<String, Workflow> workflows = new HashMap<>();

        String[] split = FileService.readStringFromDocument(fileName).split("\n\n");

        split[0].lines().forEach(line -> {
            var w = new Workflow(line);
            var n = w.getName();
            workflows.put(n, w);
        });

        List<Part> parts = split[1].lines().map(Part::new).toList();

        return parts.stream()
                .map(p -> getResult(p, workflows))
                .reduce(0, Integer::sum);
    }

    public static long getDistinctCombo(String fileName) {
        HashMap<String, Workflow> workflows = new HashMap<>();

        String[] split = FileService.readStringFromDocument(fileName).split("\n\n");

        split[0].lines().forEach(line -> {
            var w = new Workflow(line);
            var n = w.getName();
            workflows.put(n, w);
        });

        var queue = new LinkedList<RangeAndWorkflow>();
        queue.add(new RangeAndWorkflow(new Range(), "in"));

        long combo = 0L;

        List<Range> acceptedRange = new ArrayList<>();

        while (!queue.isEmpty()) {
            var curr = queue.remove();
            if (curr.w().compareTo("A") == 0) {
                acceptedRange.add(curr.r());
                combo += curr.r().getDistinctCombo();
            } else if (curr.w().compareTo("R") == 0) {
                // Do nothing
            } else {
                queue.addAll(curr.r().getNextRangeAndWorkflow(workflows.get(curr.w()).getRules()));
            }
        }

        return combo;
    }
}
