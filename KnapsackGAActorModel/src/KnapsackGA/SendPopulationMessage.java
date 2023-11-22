package KnapsackGA;

import library.Message;

public class SendPopulationMessage extends Message {
    private Individual[] population;
    private int counterGen;
    public SendPopulationMessage(Individual[] population, int counterGen) {
        super();
        this.population = population;
        this.counterGen=counterGen;
    }

    public Individual[] getPopulation() {
        return population;
    }

    public int getGeneration() {
        return counterGen;
    }
}
