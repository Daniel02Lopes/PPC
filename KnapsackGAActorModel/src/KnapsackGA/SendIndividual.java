package KnapsackGA;

import library.Message;

public class SendIndividual extends Message {
        private final int index;
        private final Individual individual;
    public SendIndividual(int i, Individual individual) {
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
