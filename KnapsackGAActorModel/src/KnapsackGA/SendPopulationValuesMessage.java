package KnapsackGA;

import library.Message;

public class SendPopulationValuesMessage extends Message {
        private final int index;
        private final Individual individual;
    public SendPopulationValuesMessage(int i, Individual individual) {
            this.index=i;
            this.individual=individual;
    }

    public int getIndex() {
        return index;
    }

    public Individual getIndividual() {
        return individual;
    }
}
