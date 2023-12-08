package services;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Node {
    private final String current;
    private final String left;
    private final String right;

    Node(String current, String left, String right) {
        this.current = current;
        this.left = left;
        this.right = right;
    }

    public String getCurrent() {
        return current;
    }

    boolean isStartNode() {
        return current.charAt(2) == 'A';
    }

    String getNode(Character direction) {
        return direction.equals('L') ? left : right;
    }

    @Override
    public String toString() {
        return "%s = (%s, %s)".formatted(current, left, right);
    }
}

public class MapNetworkService {

    public static final String FIRST_NODE = "AAA";
    public static final String END_NODE = "ZZZ";

    private static String getDirections(String fileName) {
        return FileService.readStringFromDocument(fileName).lines().findFirst().orElse("");
    }

    private static HashMap<String, Node> getNodes(String fileName) {
        var split = FileService.readStringFromDocument(fileName).split("\n\n");
        String nodeStrings = split[1];

        var nodes = new HashMap<String, Node>();

        nodeStrings.lines().forEach(line -> {
            Pattern p = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");
            Matcher m = p.matcher(line);
            if (m.matches()) {
                nodes.put(
                        m.group(1),
                        new Node(m.group(1), m.group(2), m.group(3))
                );
            }
        });
        return nodes;
    }

    public static long getNumOfSteps(String fileName) {
        return getCountFromNode(
                getDirections(fileName),
                getNodes(fileName),
                FIRST_NODE,
                true
        );
    }

    public static long getNumOfSimultaneouslySteps(String fileName) {
        var directions = getDirections(fileName);
        var nodes = getNodes(fileName);

        return nodes.values().stream()
                .filter(Node::isStartNode)
                .map(Node::getCurrent)
                .map(node -> getCountFromNode(directions, nodes, node, false))
                .reduce(1L, MapNetworkService::getLCM);
    }

    private static long getCountFromNode(
            String directions,
            HashMap<String, Node> nodes,
            String startNode,
            boolean useStrictCompare
    ) {
        var currNode = startNode;
        int count = 0;
        int directionIndex = 0;

        while (useStrictCompare ? currNode.compareTo(END_NODE) != 0 : currNode.charAt(2) != 'Z') {
            var nextNode = nodes.get(currNode).getNode(directions.charAt(directionIndex));
            count++;

            if (useStrictCompare ? currNode.compareTo(END_NODE) == 0 : currNode.charAt(2) == 'Z') {
                return count;
            } else {
                currNode = nextNode;
                directionIndex++;
                if (directionIndex == directions.length()) {
                    directionIndex = 0;
                }
            }
        }

        return count;
    }

    static long getLCM(long num1, long num2) {
        long gcd = 1;

        for (int i = 1; i <= num1 && i <= num2; i++) {
            if (num1 % i == 0 && num2 % i == 0)
                gcd = i;
        }

        return num1 * num2 / gcd;
    }
}
