package KnapsackGA;

import library.Actor;
import library.Message;

public class KnapsackGAActorBestIndividual extends Actor {
    private final int generation;
    private Individual[] population;

    public KnapsackGAActorBestIndividual(int generation) {
        this.generation = generation;
    }
    @Override
    protected void handleMessage(Message m) {
        if (m instanceof FindBestIndividualMessage) {
            Individual best = population[0];
            for (Individual other : population) {
                if (other.fitness > best.fitness) {
                    best = other;
                }
            }
            System.out.println("Best at generation " + generation + " is " + best + " with " + best.fitness);
            getAddress().sendMessage(new SendBestIndividualMessage(best));
        } else if (m instanceof SendPopulationValuesMessage m1) {
            population[m1.getIndex()] = m1.getIndividual();
        }
    }
}
