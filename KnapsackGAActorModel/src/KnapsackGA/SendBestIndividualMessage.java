package KnapsackGA;

import library.Message;

public class SendBestIndividualMessage extends Message {
    private final Individual best;
    public SendBestIndividualMessage(Individual best) {
        super();
        this.best = best;
    }

    public Individual getBest() {
        return best;
    }
}
