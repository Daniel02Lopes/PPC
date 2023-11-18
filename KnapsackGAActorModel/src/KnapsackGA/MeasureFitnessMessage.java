package KnapsackGA;

import library.Message;

public class MeasureFitnessMessage extends Message {
    private final int counterGen;

    public MeasureFitnessMessage(int counterGen) {
        this.counterGen = counterGen;
    }

    public int getCounterGen() {
        return this.counterGen;
    }
}
