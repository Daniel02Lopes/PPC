package KnapsackGA;

import library.Actor;
import library.Address;
import library.Message;

import java.util.Random;

public class KnapsackGAActorCrossOver extends Actor {
    private  Individual[] newPopulation = new Individual[getPopSize()];
    private Individual[] population1 = new Individual[getPopSize()];

    public KnapsackGAActorCrossOver(Address address) {
        super(address);
    }

    @Override
    protected void handleMessage(Message m) {
        if(m instanceof SendBestIndividualMessage mi){
            newPopulation[0] = mi.getBest(); // The best individual remains
            getSupervisor().sendMessage(new SendIndividualMessage(0,mi.getBest()));
            for (int i = 1; i < getPopSize(); i++) {
                // We select two parents, using a tournament.
                Individual parent1 = tournament(getTournamentSize(), getR());
                Individual parent2 = tournament(getTournamentSize(), getR());
                newPopulation[i] = parent1.crossoverWith(parent2, getR());
                getSupervisor().sendMessage(new SendIndividualMessage(i,newPopulation[i]));
            }
            getSupervisor().sendMessage(new MutatePopultionMessage());
        }
        else if (m instanceof SendPopulationMessage m1) {
            population1=m1.getPopulation();
        }
    }

    private Individual tournament(int tournamentSize, Random r) {
        /*
         * In each tournament, we select tournamentSize individuals at random, and we
         * keep the best of those.
         */
        Individual best = population1[r.nextInt(getPopSize())];
        for (int i = 0; i < tournamentSize; i++) {
            Individual other = population1[r.nextInt(getPopSize())];
            if (other.fitness > best.fitness) {
                best = other;
            }
        }
        return best;
    }
}
