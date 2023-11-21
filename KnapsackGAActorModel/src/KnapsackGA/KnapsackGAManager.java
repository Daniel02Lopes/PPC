package KnapsackGA;

import library.Actor;
import library.Message;
import library.SystemKillMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class KnapsackGAManager extends Actor {
    private static int counterGen=-1;
    private static long startTime;
    private static long endTime;
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
         kai = new KnapsackGAActorInitializer(this.getAddress());
    }

    @Override
    protected void handleMessage(Message m) {
        if (m instanceof BootstrapMessage) {
            startTime = System.nanoTime();
            kai.getAddress().sendMessage(new InitializePopulationRMessage());
        }
        else if(m instanceof SendPopulationValuesMessage) {
            if (counterGen < getnGenerations())
                kamf.getAddress().sendMessage(m);
        }
        else if(m instanceof GenerationIsFinish) {
            if (counterGen < getnGenerations()) {
                if (counterGen == getnGenerations()-1){
                    endTime = System.nanoTime();
                    kamf.getAddress().sendMessage(new SystemKillMessage());
                    kabi.getAddress().sendMessage(new SystemKillMessage());
                    kaco.getAddress().sendMessage(new SystemKillMessage());
                    kam.getAddress().sendMessage(new SystemKillMessage());
                    this.getAddress().sendMessage(new SystemKillMessage());
                }
                counterGen++;
                kamf.getAddress().sendMessage(new MeasureFitnessMessage(counterGen));
            }
        }
    }

    public long getExecutionTime() {
        return endTime-startTime;
    }

}
