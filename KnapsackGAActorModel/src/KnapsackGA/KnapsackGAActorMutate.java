package KnapsackGA;

import library.Actor;
import library.Address;
import library.Message;

public class KnapsackGAActorMutate extends Actor {

    public KnapsackGAActorMutate(Address address) {
        super(address);
    }

    @Override
    protected void handleMessage(Message m) {
        if(m instanceof SendPopulationValuesMessage mi)
            getPopulation()[mi.getIndex()]=mi.getIndividual();
        else if(m instanceof MutatePopultionMessage mi){
            System.out.println("Mutate");
            // Step4 - Mutate
            for (int i = 1; i < getPopSize(); i++) {
                if (getR().nextDouble() < getProbMutation()) {
                    getPopulation()[i].mutate(getR());
                }
            }
            for(int i=0;i<getPopSize();i++)
                getSupervisor().sendMessage(new SendPopulationValuesMessage(i, getPopulation()[i]));
            getSupervisor().sendMessage(new GenerationIsFinish());
        }

    }
}
