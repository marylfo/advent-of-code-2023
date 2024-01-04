package challenges;

import services.PulseService;

public class Day20 {
    public static void getPulseProduct() {
        var num = PulseService.getPulseProduct("20-input");
        System.out.printf("Solving Day 20 Pulse Product: %s%n", num);
    }

    public static void getButtonCountToSendLowToRx() {
        var num = PulseService.getButtonCountToSendLowToRx("20-input");
        System.out.printf("Solving Day 20 Button Press: %s%n", num);
    }
}
