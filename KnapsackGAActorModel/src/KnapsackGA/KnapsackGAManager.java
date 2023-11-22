package KnapsackGA;

import library.Actor;
import library.Message;
import library.SystemKillMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class KnapsackGAManager extends Actor {
    private int counterGen=0;
    private KnapsackGAActorMutate kam ;
    private KnapsackGAActorCrossOver kaco;
    private KnapsackGAActorBestIndividual kabi ;
    private KnapsackGAActorMeasureFitness kamf ;
    private KnapsackGAActorInitializer kai ;

    public KnapsackGAManager(){
         kam = new KnapsackGAActorMutate(this.getAddress());
         kaco = new KnapsackGAActorCrossOver(kam.getAddress());
         kabi = new KnapsackGAActorBestIndividual(kaco.getAddress());
         kamf = new KnapsackGAActorMeasureFitness(kabi.getAddress());
         kai = new KnapsackGAActorInitializer(kamf.getAddress());
    }

    @Override
    protected void handleMessage(Message m) {
        if (m instanceof BootstrapMessage) {
            kai.getAddress().sendMessage(new InitializePopulationRMessage());
        }
        else if(m instanceof SendIndividual) {
            if (counterGen < getnGenerations())
                kamf.getAddress().sendMessage(m);
        }
        else if(m instanceof GenerationIsFinish) {
            if (counterGen < getnGenerations()) {
                if (counterGen == getnGenerations()-1){
                    kamf.getAddress().sendMessage(new SystemKillMessage());
                    kabi.getAddress().sendMessage(new SystemKillMessage());
                    kaco.getAddress().sendMessage(new SystemKillMessage());
                    kam.getAddress().sendMessage(new SystemKillMessage());
                    this.getAddress().sendMessage(new SystemKillMessage());
                }
                else{
                    counterGen++;
                    kamf.getAddress().sendMessage(new MeasureFitnessMessage(counterGen));
                }
            }
        }
    }

}
