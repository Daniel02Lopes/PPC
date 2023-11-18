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
        if (m instanceof FindBestIndividualMessage m1) {
            Individual best = getPopulation()[0];
            for (Individual other : getPopulation()) {
                if (other.fitness > best.fitness) {
                    best = other;
                }
            }
            System.out.println("Best at generation " + m1.getGeneration() + " is " + best + " with " + best.fitness);
            getSupervisor().sendMessage(new SendBestIndividualMessage(best));
        } else if (m instanceof SendPopulationValuesMessage m1) {
            getPopulation()[m1.getIndex()] = m1.getIndividual();
            getSupervisor().sendMessage(m);
        }
    }
}
