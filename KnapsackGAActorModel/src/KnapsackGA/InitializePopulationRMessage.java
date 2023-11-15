package KnapsackGA;

import library.Message;

public class InitializePopulationRMessage extends Message {

    private static int pop_size;

    public InitializePopulationRMessage(int pop_size){
        InitializePopulationRMessage.pop_size =pop_size;
    }

    public static int getPop_size() {
        return pop_size;
    }

}
