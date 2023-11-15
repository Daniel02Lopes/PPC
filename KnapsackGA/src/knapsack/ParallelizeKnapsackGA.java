package knapsack;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelizeKnapsackGA {
    private static final int N_GENERATIONS = 500;
    private static final int POP_SIZE = 100000;
    private static final double PROB_MUTATION = 0.5;
    private static final int TOURNAMENT_SIZE = 3;

    private ThreadLocalRandom r = ThreadLocalRandom.current(); //adicionar ao report
    private Individual[] population = new Individual[POP_SIZE];
    private final int nThreads;

    public ParallelizeKnapsackGA(int nThreads) {
        this.nThreads = nThreads;
        populateInitialPopulationRandomly();
    }

    private void populateInitialPopulationRandomly() {
        /* Creates a new population, made of random individuals */
        ParallelLibrary.doWorkParallel((int start, int end) ->  {
            for (int i = start; i < end; i++) {
                population[i] = Individual.createRandom(r);
            }
        }, POP_SIZE, nThreads);

    }

    public void run() {
        for (int generation = 0; generation < N_GENERATIONS; generation++) {
            // Step1 - Calculate Fitness with multiple threads
            ParallelLibrary.doWorkParallel((int start, int end) -> {
                for (int i = start; i < end; i++) {
                    population[i].measureFitness();
                }
            }, POP_SIZE, this.nThreads);

            // Step2 - Print the best individual so far.

            Individual best = bestOfPopulation();

            System.out.println("Best at generation " + generation + " is " + best + " with "
                    + best.fitness);

            // Step3 - Find parents to mate (cross-over) with multiple threads
            Individual[] newPopulation = new Individual[POP_SIZE];
            newPopulation[0] = best; // The best individual remains

            ParallelLibrary.doWorkParallel((int start, int end) -> {
                start = start == 0 ? 1 : start;
                for (int i = start; i < end; i++) {
                    // We select two parents, using a tournament.
                    Individual parent1 = tournament(TOURNAMENT_SIZE, r);
                    Individual parent2 = tournament(TOURNAMENT_SIZE, r);
                        newPopulation[i] = parent1.crossoverWith(parent2, r);
                }
            }, POP_SIZE, this.nThreads);


            // Step4 - Mutate with multiple threads
            ParallelLibrary.doWorkParallel((int start, int end) -> {
                start = start == 0 ? 1 : start;
                for (int i = start; i < end; i++) {
                    if (r.nextDouble() < PROB_MUTATION) {
                            newPopulation[i].mutate(r);
                    }
                }
            }, POP_SIZE, this.nThreads);
            population = newPopulation;
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

    private Individual bestOfPopulation() {
        /*
         * Returns the best individual of the population.
         */
        final Individual[] best = {population[0]};
        ParallelLibrary.doWorkParallel((int start, int end)-> {
            for (int i = start;i<end;i++) {
                if (population[i].fitness > best[0].fitness) {
                    synchronized (best[0]){
                        best[0] = population[i];
                    }
                }
            }
        }, population.length, nThreads);

        return best[0];
    }

}
