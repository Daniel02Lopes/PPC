package KnapsackGA;

import library.Actor;
import library.Address;
import library.Message;
import library.SystemKillMessage;

import java.util.Random;

public class KnapsackGAActorInitializer extends Actor {

    public KnapsackGAActorInitializer(Address address) {
        super(address);
    }

    @Override
    protected void handleMessage(Message m) {
        if (m instanceof InitializePopulationRMessage) {
            Individual individual;
            for (int i = 0; i < getPopSize(); i++) {
                individual = Individual.createRandom(getR());
                getPopulation()[i] = individual;
                getSupervisor().sendMessage(new SendPopulationValuesMessage(i, individual));
            }
            getSupervisor().sendMessage(new MeasureFitnessMessage(0));
            this.getAddress().sendMessage(new SystemKillMessage());
        }
    }
}
