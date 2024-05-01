package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class Spring {
    private final String map;
    private final String numberOfSprings;

    public Spring(String input) {
        var string = input.split(" ");
        map = string[0];
        numberOfSprings = string[1];
    }
    
    private boolean hasUnknownSymbol(List<String> patterns) {
        return patterns.stream()
                .anyMatch(pattern -> pattern.indexOf('?') != -1);
    }
    
    public long getPossiblePatterns() {
        List<String> patterns = new ArrayList<>();
        patterns.add(map);

        while (hasUnknownSymbol(patterns)) {
            var temp = patterns.remove(0);
            var questionIndex = temp.indexOf('?');
            patterns.add(temp.substring(0, questionIndex) + "." + temp.substring(questionIndex+1));
            patterns.add(temp.substring(0, questionIndex) + "#" + temp.substring(questionIndex+1));
        }

        return patterns.stream()
                .map(pattern -> Arrays.stream(pattern.split("\\.+"))
                        .filter(springValue -> !springValue.isEmpty())
                        .map(springCombo -> String.valueOf(springCombo.length()))
                        .collect(Collectors.joining(",")))
                .filter(count -> count.equals(numberOfSprings))
                .count();
    }
}

// Reference: Recursion idea from HyperNeutrino (https://www.youtube.com/watch?v=g3Ms5e7Jdqo)

record SpringData(String map, ArrayList<Integer> numberOfSprings) {
    static SpringData of(String row) {
        var string = row.split(" ");
        return new SpringData(
                string[0],
                new ArrayList<>(Arrays.stream(string[1].split(",")).map(Integer::parseInt).toList())
        );
    }

    static SpringData ofUnfoldFive(String row) {
        var string = row.split(" ");
        var numberOfSprings = new ArrayList<Integer>();

        var oneList = Arrays.stream(string[1].split(",")).map(Integer::parseInt).toList();
        for (int i = 0; i < 5; i++) {
            numberOfSprings.addAll(oneList);
        }

        return new SpringData(
                "%s?%s?%s?%s?%s".formatted(string[0], string[0], string[0], string[0], string[0]),
                numberOfSprings
        );
    }
}

class SpringWithRecursion {
    public static HashMap<SpringData, Long> cache = new HashMap<>();

    public static ArrayList<Integer> getArrayWithoutFirstElement(final ArrayList<Integer> array) {
        ArrayList<Integer> newArray = new ArrayList<>();

        for (int i = 1; i < array.size(); i++) {
            newArray.add(array.get(i));
        }

        return newArray;
    }

    public static long getPossiblePatterns(final SpringData data) {
        var map = data.map();
        var numberOfSprings = data.numberOfSprings();

        // Base Method
        if (map.isEmpty()) {
            return (numberOfSprings.isEmpty()) ? 1 : 0;
        }

        if (numberOfSprings.isEmpty()) {
            return (map.contains("#")) ? 0 : 1;
        }

        if (cache.containsKey(data)) {
            return cache.get(data);
        }

        long result = 0;

        if (map.charAt(0) == '.' || map.charAt(0) == '?') {
            // treat it as .
            result += getPossiblePatterns(new SpringData(
                    map.length() > 1 ? map.substring(1) : "",
                    numberOfSprings));
        }

        if (map.charAt(0) == '#' || map.charAt(0) == '?') {
            // treat it as #
            if ((numberOfSprings.get(0) <= map.length()) &&
                    !(map.substring(0, numberOfSprings.get(0)).contains(".")) &&
                    (map.length() == numberOfSprings.get(0) || map.charAt(numberOfSprings.get(0)) != '#')
            ) {
                result += getPossiblePatterns(new SpringData(
                        map.length() > numberOfSprings.get(0) ? map.substring(numberOfSprings.get(0) + 1) : "",
                        getArrayWithoutFirstElement(numberOfSprings)));
            }
        }

        cache.put(data, result);

        return result;
    }
}


public class SpringService {
    public static long getSumOfPossibleCombo(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(Spring::new)
                .map(Spring::getPossiblePatterns)
                .reduce(0L, Long::sum);
    }

    public static long getSumOfPossibleComboRecursion(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(SpringData::of)
                .map(SpringWithRecursion::getPossiblePatterns)
                .reduce(0L, Long::sum);
    }

    public static long getSumOfPossibleUnFoldComboRecursion(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(SpringData::ofUnfoldFive)
                .map(SpringWithRecursion::getPossiblePatterns)
                .reduce(0L, Long::sum);
    }
}
