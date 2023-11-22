package KnapsackGA;

import library.Actor;
import library.Address;
import library.Message;

public class KnapsackGAActorBestIndividual extends Actor {
    public KnapsackGAActorBestIndividual( Address address) {
        super(address);
    }
    @Override
    protected void handleMessage(Message m) {
        if (m instanceof SendPopulationMessage m1) {
            getSupervisor().sendMessage(m1);
            Individual best = m1.getPopulation()[0];
            for (Individual other : m1.getPopulation()) {
                if (other.fitness > best.fitness) {
                    best = other;
                }
            }
            System.out.println("Best at generation " + m1.getGeneration() + " is " + best + " with " + best.fitness);
            getSupervisor().sendMessage(new SendBestIndividualMessage(best));
        }
    }
}
