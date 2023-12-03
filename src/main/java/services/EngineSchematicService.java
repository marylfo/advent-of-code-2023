package services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EngineSchematicService {

    public static final String NON_DIGITS_REGEX = "\\D+";
    public static final String PERIOD_SYMBOL_REGEX = "\\.";
    public static final String DIGIT_REGEX = "\\d+";
    public static final String ASTERIS_SYMBOL = "*";

    public static int getSumOfPartNumbers(String fileName) {
        List<String> engineSchematicData = EngineSchematicService.getEngineSchematicData(fileName);
        List<List<Integer>> numbersLocation = EngineSchematicService.getNumberLocations(engineSchematicData);

        return numbersLocation.stream()
                .map(numberLocation -> {
                    String numberBlock = EngineSchematicService.getAdjacentCharactersOfNumber(
                            numberLocation.get(0),
                            numberLocation.get(1),
                            numberLocation.get(2),
                            engineSchematicData,
                            engineSchematicData.size(),
                            engineSchematicData.get(0).length()
                    );

                    return EngineSchematicService.isPartNumber(numberBlock) ? numberLocation.get(3) : 0;
                }).reduce(0, Integer::sum);
    }

    public static int getSumOfGearRations(String fileName) {
        List<String> engineSchematicData = EngineSchematicService.getEngineSchematicData(fileName);
        List<List<Integer>> asterisksLocation = EngineSchematicService.getAsteriskLocations(engineSchematicData);

        return asterisksLocation.stream()
                .map(asteriskLocation -> EngineSchematicService.getGearRatio(
                        asteriskLocation.get(0),
                        asteriskLocation.get(1),
                        engineSchematicData,
                        engineSchematicData.size(),
                        engineSchematicData.get(0).length()
                ))
                .reduce(0, Integer::sum);
    }

    static List<String> getEngineSchematicData(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .toList();
    }

    static List<List<Integer>> getNumberLocations(List<String> engineSchematicData) {
        List<List<Integer>> numberLocations = new ArrayList<>();

        for (int listIndex = 0; listIndex < engineSchematicData.size(); listIndex++) {
            String[] numbers = engineSchematicData.get(listIndex).split(NON_DIGITS_REGEX);
            int searchIndex = 0;
            for (String number: numbers) {
                if (number.length() > 0) {
                    List<Integer> numberLocation = new ArrayList<>();
                    int numberIndex = engineSchematicData.get(listIndex).indexOf(number, searchIndex);
                    int numberLength = number.length();

                    numberLocation.add(listIndex);
                    numberLocation.add(numberIndex);
                    numberLocation.add(numberLength);
                    numberLocation.add(Integer.parseInt(number));

                    numberLocations.add(numberLocation);

                    searchIndex = numberIndex + numberLength;
                }
            }
        }

        return numberLocations;
    }

    static String getAdjacentCharactersOfNumber(
            int numberListIndex,
            int numberStringStartIndex,
            int numberLength,
            List<String> engineSchematic,
            int engineSchematicSize,
            int engineSchematicLineLength
    ) {
        boolean isTopReachable = (numberListIndex != 0);
        boolean isBottomReachable = numberListIndex != (engineSchematicSize - 1);
        int leftIncrement = (numberStringStartIndex == 0) ? 0 : 1;
        int rightIncrement = ((numberStringStartIndex + numberLength) >= engineSchematicLineLength) ? 0 : 1;

        String adjacentCharacters = "";

        if (isTopReachable) {
            adjacentCharacters += engineSchematic.get(numberListIndex - 1)
                    .substring(
                            numberStringStartIndex - leftIncrement,
                            numberStringStartIndex + numberLength + rightIncrement
                    );
        }

        adjacentCharacters += engineSchematic.get(numberListIndex)
                .substring(
                        numberStringStartIndex - leftIncrement,
                        numberStringStartIndex + numberLength + rightIncrement
                );

        if (isBottomReachable) {
            adjacentCharacters += engineSchematic.get(numberListIndex + 1)
                    .substring(
                            numberStringStartIndex - leftIncrement,
                            numberStringStartIndex + numberLength + rightIncrement
                    );
        }

        return adjacentCharacters;
    }

    static boolean isPartNumber(String adjacentCharacters) {
        return adjacentCharacters
                .replaceAll(PERIOD_SYMBOL_REGEX, "")
                .replaceAll(DIGIT_REGEX, "")
                .length() > 0;
    }

    static List<List<Integer>> getAsteriskLocations(List<String> engineSchematicData) {
        List<List<Integer>> asteriskLocations = new ArrayList<>();

        for (int listIndex = 0; listIndex < engineSchematicData.size(); listIndex++) {
            int searchIndex = 0;
            int location = engineSchematicData.get(listIndex).indexOf(ASTERIS_SYMBOL, searchIndex);
            while (location != -1) {
                List<Integer> asteriskLocation = new ArrayList<>();
                asteriskLocation.add(listIndex);
                asteriskLocation.add(location);

                asteriskLocations.add(asteriskLocation);

                searchIndex = location + 1;
                location = engineSchematicData.get(listIndex).indexOf(ASTERIS_SYMBOL, searchIndex);
            }
        }

        return asteriskLocations;
    }

    static int getGearRatio(
            int numberListIndex,
            int asteriskIndex,
            List<String> engineSchematic,
            int engineSchematicSize,
            int engineSchematicLineLength
    ) {
        boolean isTopReachable = (numberListIndex != 0);
        boolean isBottomReachable = (numberListIndex != (engineSchematicSize - 1));
        boolean isLeftReachable = (asteriskIndex > 0);
        boolean isRightReachable = ((asteriskIndex + 1) < engineSchematicLineLength);

        Set<Integer> partNumbers = new HashSet<>();

        if (isTopReachable &&
                isLeftReachable &&
                isDigitFromLocation(numberListIndex - 1, asteriskIndex - 1, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex - 1,
                    asteriskIndex - 1,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }

        if (isTopReachable &&
                isDigitFromLocation(numberListIndex - 1, asteriskIndex, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex - 1,
                    asteriskIndex,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }

        if (isTopReachable &&
                isRightReachable &&
                isDigitFromLocation(numberListIndex - 1, asteriskIndex + 1, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex - 1,
                    asteriskIndex + 1,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }

        if (isLeftReachable &&
                isDigitFromLocation(numberListIndex, asteriskIndex - 1, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex,
                    asteriskIndex - 1,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }

        if (isRightReachable &&
                isDigitFromLocation(numberListIndex, asteriskIndex + 1, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex,
                    asteriskIndex + 1,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }


        if (isBottomReachable &&
                isLeftReachable &&
                isDigitFromLocation(numberListIndex + 1, asteriskIndex - 1, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex + 1,
                    asteriskIndex - 1,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }

        if (isBottomReachable &&
                isDigitFromLocation(numberListIndex + 1, asteriskIndex, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex + 1,
                    asteriskIndex,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }

        if (isBottomReachable &&
                isRightReachable &&
                isDigitFromLocation(numberListIndex + 1, asteriskIndex + 1, engineSchematic)) {
            partNumbers.add(getNumberFromLocation(
                    numberListIndex + 1,
                    asteriskIndex + 1,
                    engineSchematic,
                    engineSchematicLineLength
            ));
        }

        return (partNumbers.size() == 2) ?
                partNumbers.stream().reduce(1, (x, y) -> x * y) :
                0;
    }

    static int getNumberFromLocation(
            int numberListIndex,
            int index,
            List<String> engineSchematic,
            int engineSchematicLineLength
    ) {
        StringBuilder number = new StringBuilder(
                "%c".formatted(engineSchematic.get(numberListIndex).charAt(index))
        );
        int pointer = index - 1;
        while (
                (pointer >= 0) &&
                isDigitFromLocation(numberListIndex, pointer, engineSchematic)
        ) {
            number.insert(0, engineSchematic.get(numberListIndex).charAt(pointer));
            pointer--;
        }

        pointer = index + 1;
        while (
                (pointer < engineSchematicLineLength) &&
                 isDigitFromLocation(numberListIndex, pointer, engineSchematic)
        ) {
            number.append(engineSchematic.get(numberListIndex).charAt(pointer));
            pointer++;
        }

        return Integer.parseInt(number.toString());
    }

    static boolean isDigitFromLocation(
            int numberListIndex,
            int index,
            List<String> engineSchematic
    ) {
        return Character.isDigit(
                engineSchematic.get(numberListIndex).charAt(index)
        );
    }
}
