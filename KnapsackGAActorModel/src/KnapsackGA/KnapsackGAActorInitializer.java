package KnapsackGA;

import library.Actor;
import library.Message;

import java.util.Random;

public class KnapsackGAActorInitializer extends Actor {
    private Individual[] population;
    private Random r = new Random();

    @Override
    protected void handleMessage(Message m) {
        if (m instanceof InitializePopulationRMessage mi) {
            System.out.println("Receive InitializePopulationRMessage");
            Individual individual;
            for (int i = 0; i < mi.getPop_size(); i++) {
                individual = Individual.createRandom(r);
                getAddress().sendMessage(new SendPopulationValuesMessage(i, individual));
                population[i] = individual;
            }

        }
    }
}
