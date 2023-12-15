package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

class Step {
    private String label;
    private int boxNumber;
    private boolean isToRemoveLabel;
    private int focalLength;

    Step(String sequence) {
        if (sequence.contains("-")) {
            isToRemoveLabel = true;
            label = sequence.substring(0, sequence.indexOf('-'));
        } else {
            isToRemoveLabel = false;
            label = sequence.substring(0, sequence.indexOf('='));
            focalLength = Integer.parseInt(sequence.substring(sequence.indexOf('=') + 1));
        }
        boxNumber = getHashValue(label);
    }

    static int getHashValue(String sequence) {
        int value = 0;

        for (int i = 0; i < sequence.length(); i++) {
            value = ((value + sequence.codePointAt(i)) * 17) % 256;
        }

        return value;
    }

    int getBoxNumber() {
        return boxNumber;
    }

    int getFocalLength() {
        return focalLength;
    }

    boolean isAddToBox() {
        return !isToRemoveLabel;
    }

    boolean isHavingSameLabel(Step otherStep) {
        return (label.compareTo(otherStep.label) == 0);
    }

    @Override
    public String toString() {
        return "[%s  %s]".formatted(label, focalLength);
    }
}

public class HolidayHashService {
    public static int getSum(String fileName) {
        String[] steps = FileService.readStringFromDocument(fileName)
                .split(",");

        return Arrays.stream(steps)
                .map(Step::getHashValue)
                .reduce(0, Integer::sum);
    }

    public static int getFocusPower(String fileName) {
        var boxes = getBoxes(fileName);
        int sum = 0;
        for (var boxNumber : boxes.keySet()) {
            List<Step> lens = boxes.get(boxNumber);
            sum += IntStream.range(0, lens.size())
                    .mapToObj(slot -> (boxNumber + 1) * (slot + 1) * lens.get(slot).getFocalLength())
                    .reduce(0, Integer::sum);
        }

        return sum;
    }

    static HashMap<Integer, List<Step>> getBoxes(String fileName) {
        HashMap<Integer, List<Step>> boxes = new HashMap<>();

        var sequences = FileService.readStringFromDocument(fileName)
                .split(",");

        for (var sequence: sequences) {
            var step = new Step(sequence);
            var boxNumber = step.getBoxNumber();

            if (step.isAddToBox()) {
                if (boxes.containsKey(boxNumber) && boxes.get(boxNumber).size() > 0) {
                    var boxContent = boxes.get(boxNumber);
                    var isAdded = false;
                    for (var i = 0; i < boxContent.size(); i++) {
                        if (!isAdded && boxContent.get(i).isHavingSameLabel(step)) {
                            boxContent.remove(i);
                            boxContent.add(i, step);
                            isAdded = true;
                        }
                    }
                    if (!isAdded) {
                        boxContent.add(step);
                    }
                } else {
                    var boxContent = new ArrayList<Step>();
                    boxContent.add(step);
                    boxes.put(boxNumber, boxContent);
                }
            } else {
                if (boxes.containsKey(boxNumber)) {
                    var boxContent = boxes.get(boxNumber);
                    var isRemoved = false;
                    for (var i = 0; i < boxContent.size(); i++) {
                        if (!isRemoved && boxContent.get(i).isHavingSameLabel(step)) {
                            boxContent.remove(i);
                            isRemoved = true;
                        }
                    }

                    if (boxContent.size() == 0) {
                        boxes.remove(boxNumber);
                    }
                }
            }
        }
        return boxes;
    }
}
