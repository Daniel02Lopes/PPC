package KnapsackGA;

import library.Message;

public class FindBestIndividualMessage extends Message {
    private final int gen;

    public FindBestIndividualMessage(int gen){
        this.gen= gen;
    }
    public int getGeneration() {
        return gen;
    }
}
