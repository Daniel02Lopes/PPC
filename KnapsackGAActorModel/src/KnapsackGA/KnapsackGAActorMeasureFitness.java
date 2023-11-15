package KnapsackGA;

import library.Actor;
import library.Message;
import library.SystemKillMessage;

public class KnapsackGAActorMeasureFitness extends Actor {
    private int size;
    Individual[] population;
    @Override
    protected void handleMessage(Message m) {

        if (m instanceof MeasureFitnessMessage) {
            for (int i = 0; i < size; i++) {
                population[i].measureFitness();
                getAddress().sendMessage(new SendPopulationValuesMessage(i,population[i]));
            }

            getAddress().sendMessage(new FindBestIndividualMessage());
        }
        else if(m instanceof InitializePopulationRMessage mi){
            this.size = mi.getPop_size();
        }

        else if (m instanceof SendPopulationValuesMessage m1) {
                population[m1.getIndex()] = m1.getIndividual();
        }
    }
}
