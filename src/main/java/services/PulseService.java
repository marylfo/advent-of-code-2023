package services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

enum PULSE {
    LOW,
    HIGH
}

class Module {
    private final String name;
    private final List<String> nextModuleNames;
    private List<Module> nextModules;

    Module(String name, List<String> nextModuleNames) {
        this.name = name;
        this.nextModuleNames = nextModuleNames;
    }

    boolean hasNextModules(String m) {
        return nextModuleNames.contains(m);
    }

    boolean hasNextModules(Module m) {
        return nextModules.contains(m);
    }

    public String getName() {
        return name;
    }

    public List<String> getNextModuleNames() {
        return nextModuleNames;
    }

    public List<Module> getNextModules() {
        return nextModules;
    }

    void setNextModules(List<Module> nextModules) {
        this.nextModules = nextModules;
    }

    PULSE getOutSignal() {
        return null;
    }
}

class BroadcasterModule extends Module {
    BroadcasterModule(String name, List<String> nextModuleNames) {
        super(name, nextModuleNames);
    }

    @Override
    PULSE getOutSignal() {
        return PULSE.LOW;
    }
}

class FlipFlopModule extends Module {
    private PULSE outSignal;

    FlipFlopModule(String name, List<String> nextModuleNames) {
        super(name, nextModuleNames);
        outSignal = PULSE.LOW;
    }

    public void flipOutSignal() {
        outSignal = (outSignal == PULSE.LOW) ? PULSE.HIGH : PULSE.LOW;
    }

    @Override
    PULSE getOutSignal() {
        return outSignal;
    }
}

class ConjunctionModule extends Module{
    private final HashMap<String, PULSE> memory;

    ConjunctionModule(String name, List<String> nextModuleNames) {
        super(name, nextModuleNames);
        memory = new HashMap<>();
    }

    void createMemory(String name) {
        memory.put(name, PULSE.LOW);
    }

    void updateMemory(String name, PULSE p) {
        memory.put(name, p);
    }

    List<String> getInModules() {
        return memory.keySet().stream().toList();
    }

    @Override
    PULSE getOutSignal() {
        return memory.values().stream().allMatch(p -> p == PULSE.HIGH) ?
                PULSE.LOW :
                PULSE.HIGH;
    }
}

public class PulseService {
    record PulseSignal (Module from, Module to, PULSE signal) {}

    public static int getPulseProduct(String fileName) {
        return getPulseProductFromModules(getModules(fileName), 1000);
    }

    public static long getButtonCountToSendLowToRx(String fileName) {
        var modules = getModules(fileName);
        var secondLayer = getSecondLayersFromRx(modules);

        return secondLayer.stream()
                .map(module -> getButtonCountThatSendHighFromModule(fileName, module))
                .reduce(1L, (a, b) -> a*b);
    }

    static HashMap<String, Module> getModules(String fileName) {
        var modules = new HashMap<String, Module>();

        FileService.readStringFromDocument(fileName)
                .lines()
                .forEach(line -> {
                    var split = line.split(" -> ");
                    var fromModule = split[0];
                    var toModules = List.of(split[1].split(", "));

                    if (fromModule.compareTo("broadcaster") == 0) {
                        modules.put(fromModule, new BroadcasterModule(fromModule, toModules));
                    } else if (fromModule.charAt(0) == '%') {
                        modules.put(
                                fromModule.substring(1),
                                new FlipFlopModule(fromModule.substring(1), toModules)
                        );
                    } else if (fromModule.charAt(0) == '&') {
                        modules.put(
                                fromModule.substring(1),
                                new ConjunctionModule(fromModule.substring(1), toModules)
                        );
                    }
                });

        modules.values().forEach(module -> module.setNextModules(
                module.getNextModuleNames().stream()
                        .map(name ->
                            modules.get(name) != null ?
                                    modules.get(name) :
                                    new Module(name, null)
                        )
                        .toList())
        );

        modules.values().stream()
                .filter(module -> module instanceof ConjunctionModule)
                .forEach(conjunction -> {
                    modules.values().stream()
                            .filter(module -> module.hasNextModules(conjunction))
                            .forEach(module -> ((ConjunctionModule) conjunction).createMemory(module.getName()));
                });

        return modules;
    }

    private static int getPulseProductFromModules(HashMap<String, Module> modules, int clicks) {
        int lowPulseCount = 0, highPulseCount = 0;
        var queue = new LinkedList<PulseSignal>();

        for (var i = 0; i < clicks; i++) {
            lowPulseCount++;

            modules.get("broadcaster").getNextModules()
                    .forEach(next -> queue.add(new PulseSignal(
                            modules.get("broadcaster"),
                            next,
                            PULSE.LOW
                    )));

            while (!queue.isEmpty()) {
                var currSignal = queue.remove();

                if (currSignal.signal ==  PULSE.LOW) {
                    lowPulseCount++;
                } else {
                    highPulseCount++;
                }

                if (currSignal.to instanceof FlipFlopModule && currSignal.signal == PULSE.LOW) {
                    ((FlipFlopModule) currSignal.to).flipOutSignal();

                    currSignal.to.getNextModules()
                            .forEach(next -> queue.add(
                                    new PulseSignal(currSignal.to, next, currSignal.to.getOutSignal()))
                            );

                } else if (currSignal.to instanceof ConjunctionModule) {
                    ((ConjunctionModule) currSignal.to).updateMemory(currSignal.from.getName(), currSignal.signal);

                    currSignal.to.getNextModules()
                            .forEach(next -> queue.add(
                                    new PulseSignal(currSignal.to, next, currSignal.to.getOutSignal()))
                            );
                }
            }
        }

        return lowPulseCount * highPulseCount;
    }

    private static List<String> getSecondLayersFromRx(HashMap<String, Module> modules) {
        var oneLayerFromRx = modules.values().stream().filter(module -> module.hasNextModules("rx")).toList().get(0);
        return ((ConjunctionModule) oneLayerFromRx).getInModules();
    }

    private static long getButtonCountThatSendHighFromModule(String fileName, String module) {
        var modules = getModules(fileName);

        long count = 0;
        var queue = new LinkedList<PulseSignal>();
        boolean isSendLow = false;

        while (!isSendLow) {
            count++;

            modules.get("broadcaster").getNextModules()
                    .forEach(next -> queue.add(new PulseSignal(
                            modules.get("broadcaster"),
                            next,
                            PULSE.LOW
                    )));

            while (!queue.isEmpty()) {
                var currSignal = queue.remove();

                if (currSignal.from.getName().compareTo(module) == 0 && currSignal.signal == PULSE.HIGH) {
                    isSendLow = true;
                }

                if (currSignal.to instanceof FlipFlopModule && currSignal.signal == PULSE.LOW) {
                    ((FlipFlopModule) currSignal.to).flipOutSignal();

                    currSignal.to.getNextModules()
                            .forEach(next -> queue.add(
                                    new PulseSignal(currSignal.to, next, currSignal.to.getOutSignal()))
                            );

                } else if (currSignal.to instanceof ConjunctionModule) {
                    ((ConjunctionModule) currSignal.to).updateMemory(currSignal.from.getName(), currSignal.signal);

                    currSignal.to.getNextModules()
                            .forEach(next -> queue.add(
                                    new PulseSignal(currSignal.to, next, currSignal.to.getOutSignal()))
                            );
                }
            }
        }

        return count;
    }
}
