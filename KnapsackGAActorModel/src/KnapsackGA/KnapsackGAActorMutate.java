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
        if(m instanceof SendIndividualMessage mi) {
            getPopulation()[mi.getIndex()] = mi.getIndividual();
            if (getR().nextDouble() < getProbMutation()) {
                getPopulation()[mi.getIndex()].mutate(getR());
                this.getSupervisor().sendMessage(new SendIndividualMessage(mi.getIndex(), getPopulation()[mi.getIndex()]));
            }
        }
        else if(m instanceof MutatePopultionMessage){
            this.getSupervisor().sendMessage(new GenerationIsFinish());
        }
    }
}
