package knapsack;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        int nThreads = Runtime.getRuntime().availableProcessors();
        runForDataAnalysis(1,nThreads);
    }

    private static long runSingleSequenciallyExecution() {
        KnapsackGA ga = new KnapsackGA();
        long startTime = System.nanoTime();
        ga.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long runSingleParallelizeExecution(int nThreads) {
        ParallelizeKnapsackGA pga = new ParallelizeKnapsackGA(nThreads);
        long startTime = System.nanoTime();
        pga.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
    private static long runSingleParallelizeV2Execution(int nThreads) {
        ParallelizeKnapsackGAWithPhaser pgav2 = new ParallelizeKnapsackGAWithPhaser(nThreads);
        long startTime = System.nanoTime();
        pgav2.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static void runForDataAnalysis(int numberExecutions,int nThreads) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("execution_times.csv"))) {
            StringBuilder sb = new StringBuilder("ExecutionIndex,SequentialExecutionTime(ns)");
            for (int j = 2; j < nThreads; j *= 2) {
                sb.append(",ParallelExecutionTime").append(j).append("Threads(ns)");
            }
            sb.append(",ParallelExecutionTime").append(nThreads).append("Threads(ns)");

            for (int j = 2; j < nThreads; j *= 2) {
                sb.append(",ParallelPhaserTimeExecutionWith").append(j).append("Threads(ns)");
            }
            sb.append(",ParallelPhaserTimeExecutionWith").append(nThreads).append("Threads(ns)\n");
            writer.write(sb.toString()); // Header
            for (int i = 1; i <= numberExecutions; i++) {
                sb = new StringBuilder();
                long sequentialTime = runSingleSequenciallyExecution();
                for (int j = 2; j < nThreads; j *= 2) {
                    sb.append(",").append(runSingleParallelizeExecution(j));
                }
                sb.append(",").append(runSingleParallelizeExecution(nThreads));
                for (int j = 2; j < nThreads; j *= 2) {
                    sb.append(",").append(runSingleParallelizeV2Execution(j));
                }
                sb.append(",").append(runSingleParallelizeV2Execution(nThreads));
                // Write the run number and execution time to the CSV file
                writer.write(i + "," + sequentialTime + sb.toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
