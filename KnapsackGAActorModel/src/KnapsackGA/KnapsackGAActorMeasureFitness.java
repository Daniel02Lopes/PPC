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
            getSupervisor().sendMessage(new SendPopulationMessage(getPopulation(),mi.getCounterGen()));
        }
        else if (m instanceof SendPopulationValuesMessage m1) {
                getPopulation()[m1.getIndex()] = m1.getIndividual();
                getPopulation()[m1.getIndex()].measureFitness();
            //System.out.println();
            //getSupervisor().sendMessage(new SendPopulationValuesMessage(m1.getIndex(),getPopulation()[m1.getIndex()]));
        }
    }
}
