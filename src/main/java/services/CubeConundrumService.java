package services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

enum Colour {
    RED("red"),
    BLUE("blue"),
    GREEN("green");

    final String value;
    Colour(String value) {
        this.value = value;
    }

    public static Colour fromString(String colour) {
        for (Colour c: Colour.values()) {
            if ( colour.compareTo(c.value) == 0) {
                return c;
            }
        }
        return null;
    }
}

public class CubeConundrumService {

    public static HashMap<Colour, Integer> BAG_CUBES = new HashMap<>();

    static {
        BAG_CUBES.put(Colour.RED, 12);
        BAG_CUBES.put(Colour.GREEN, 13);
        BAG_CUBES.put(Colour.BLUE, 14);
    }

    public static int getSumOfPossibleGameIds(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(CubeConundrumService::getGameIdIfGameIsPossible)
                .reduce(0, Integer::sum);
    }

    public static int getSumOfPowerOfGames(String fileName) {
        return FileService.readStringFromDocument(fileName)
                .lines()
                .map(CubeConundrumService::getSumOfPowerOfAGame)
                .reduce(0, Integer::sum);
    }

    static HashMap<Colour, Integer> getOneGameRecord(String str) {
        HashMap<Colour, Integer> gameRecord = new HashMap<>();

        Arrays.stream(str.split(", ")).forEach(cubeOccurrence -> {
            var cubeOccurrenceSplit = cubeOccurrence.split(" ");
            gameRecord.put(
                    Colour.fromString(cubeOccurrenceSplit[1]),
                    Integer.valueOf(cubeOccurrenceSplit[0])
            );
        });

        return gameRecord;
    }

    static boolean isPossibleGame(List<HashMap<Colour, Integer>> gameRecord) {
        for (var round: gameRecord) {
            for (var colour: round.keySet()) {
                if (round.get(colour) > BAG_CUBES.get(colour)) {
                    return false;
                }
            }
        }
        return true;
    }

    static int getGameIdIfGameIsPossible(String str) {
        int gameId = Integer.parseInt(str.substring("Game ".length(), str.indexOf(":")));

        String gameDataStr = str.substring(str.indexOf(": ") + 2);
        var list = Arrays.stream(gameDataStr.split("; "))
                .map(CubeConundrumService::getOneGameRecord)
                .toList();

        return isPossibleGame(list) ? gameId : 0;
    }

    static int getSumOfPowerOfAGame(String str) {
        String gameDataStr = str.substring(str.indexOf(": ") + 2);
        var list = Arrays.stream(gameDataStr.split("; "))
                .map(CubeConundrumService::getOneGameRecord)
                .toList();

        return getPowerOfAGame(list);
    }

    static int getPowerOfAGame(List<HashMap<Colour, Integer>> gameRecord) {
        var fewestCubesBag = new HashMap<Colour, Integer>();
        fewestCubesBag.put(Colour.RED, 0);
        fewestCubesBag.put(Colour.GREEN, 0);
        fewestCubesBag.put(Colour.BLUE, 0);

        for (var round: gameRecord) {
            for (var colour: round.keySet()) {
                if (round.get(colour) > fewestCubesBag.get(colour)) {
                    fewestCubesBag.put(colour, round.get(colour));
                }
            }
        }

        return fewestCubesBag.get(Colour.RED) * fewestCubesBag.get(Colour.GREEN) * fewestCubesBag.get(Colour.BLUE);
    }
}
