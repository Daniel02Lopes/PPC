package knapsack;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelizeKnapsackGAWithPhaser {

    private static final int N_GENERATIONS = 500;
    private static final int POP_SIZE = 100000;
    private static final double PROB_MUTATION = 0.5;
    private static final int TOURNAMENT_SIZE = 3;

    private ThreadLocalRandom r = ThreadLocalRandom.current();

    private final int nThreads;

    private Individual[] population = new Individual[POP_SIZE];

    public ParallelizeKnapsackGAWithPhaser(int nThreads) {
        this.nThreads = nThreads;
        populateInitialPopulationRandomly();
    }

    private synchronized void populateInitialPopulationRandomly() {
        ParallelLibrary.doWorkParallel((int start, int end) ->  {
            for (int i = start; i < end; i++) {
                population[i] = Individual.createRandom(r);
            }
        }, POP_SIZE, nThreads);
    }



    public void run() {
        final Phaser phaser = new Phaser(nThreads);
        for (int generation = 0; generation < N_GENERATIONS; generation++) {
            int finalGeneration = generation;
            ParallelLibrary.doWorkParallel((int start, int end) -> {
                Individual[] localPopulation = population;
                // Step1 - Calculate Fitness
                for (int i = start; i < end; i++) {
                    localPopulation[i].measureFitness();
                }
                phaser.arriveAndAwaitAdvance();



                // Step2 - Print the best individual so far.

                Individual best = bestOfPopulation(localPopulation);
                if (start == 0) {
                System.out.println("Best at generation " + finalGeneration + " is " + best + " with "
                        + best.fitness);
                }
                phaser.arriveAndAwaitAdvance();

                // Step3 - Find parents to mate (cross-over)
                //Individual[] newPopulation = new Individual[POP_SIZE];
                localPopulation[0] = best; // The best individual remains
                if(start == 0)
                    start=1;
                for (int i = start; i < end; i++) {
                    // We select two parents, using a tournament.
                    Individual parent1 = tournament(TOURNAMENT_SIZE, r);
                    Individual parent2 = tournament(TOURNAMENT_SIZE, r);

                    localPopulation[i] = parent1.crossoverWith(parent2, r);
                }
                phaser.arriveAndAwaitAdvance();

                // Step4 - Mutate
                for (int i = start; i < end; i++) {
                    if (r.nextDouble() < PROB_MUTATION) {
                        localPopulation[i].mutate(r);
                    }
                }
                phaser.arriveAndAwaitAdvance();
                synchronized (population) {
                    for (int i = start; i < end; i++) {
                        population[i] = localPopulation[i];
                    }
                }


            }, POP_SIZE, this.nThreads);
        }
    }

    private Individual tournament(int tournamentSize, Random r) {
        /*
         * In each tournament, we select tournamentSize individuals at random, and we
         * keep the best of those.
         */
        Individual best = population[r.nextInt(POP_SIZE)];
        for (int i = 0; i < tournamentSize; i++) {
            Individual other = population[r.nextInt(POP_SIZE)];
            if (other.fitness > best.fitness) {
                best = other;
            }
        }
        return best;
    }

    private Individual bestOfPopulation(Individual[] population) {

        /*
         * Returns the best individual of the population.
         */
        Individual best = population[0];
        for (Individual other : population) {
            if (other.fitness > best.fitness) {
                best = other;
            }
        }
        return best;

    }
}
