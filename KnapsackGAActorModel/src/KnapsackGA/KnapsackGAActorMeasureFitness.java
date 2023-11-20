package KnapsackGA;

import library.Actor;
import library.Address;
import library.Message;
import library.SystemKillMessage;

public class KnapsackGAActorMeasureFitness extends Actor {
    public KnapsackGAActorMeasureFitness(Address address) {
        super(address);
    }

    @Override
    protected void handleMessage(Message m) {

        if (m instanceof MeasureFitnessMessage mi) {
            for (int i = 0; i < getPopSize(); i++) {
                getPopulation()[i].measureFitness();
                getSupervisor().sendMessage(new SendPopulationValuesMessage(i,getPopulation()[i]));
            }
            getSupervisor().sendMessage(new FindBestIndividualMessage(mi.getCounterGen()));
        }
        else if (m instanceof SendPopulationValuesMessage m1) {
                getPopulation()[m1.getIndex()] = m1.getIndividual();
        }
    }
}
