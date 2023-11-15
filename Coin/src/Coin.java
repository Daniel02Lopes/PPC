package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Coin extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;
    public static final int LIMIT = 999;

    private Condition condition;

    private int[] coins;
    private int index;
    private int accumulator;
    private static int nCores;

    public Coin(int index, int[] coins, int accumulator, Condition condition) {
        this.coins = coins;
        this.index = index;
        this.accumulator = accumulator;
        this.condition = condition;

    }

    @Override
    protected Integer compute() {

        if (condition.check(coins.length, index, accumulator))
            return seq(coins, index, accumulator);
        if (index >= coins.length) {
            if (accumulator < Coin.LIMIT) {
                return accumulator;
            }
            return -1;
        }

        if (accumulator + coins[index] > Coin.LIMIT) {
            return -1;
        }


        Coin f1 = new Coin(index + 1, coins, accumulator, condition);
        f1.fork();
        Coin f2 = new Coin(index + 1, coins, accumulator + coins[index], condition);
        f2.fork();

        int a = f1.join();
        int b = f2.join();

        return Math.max(a, b);
    }
    // ------------------------------------------

    public static int[] createRandomCoinSet(int N) {
        int[] r = new int[N];
        for (int i = 0; i < N; i++) {
            if (i % 10 == 0) {
                r[i] = 400;
            } else {
                r[i] = 4;
            }
        }
        return r;
    }


    private static int seq(int[] coins, int index, int accumulator) {

        if (index >= coins.length) {
            if (accumulator < LIMIT) {
                return accumulator;
            }
            return -1;
        }
        if (accumulator + coins[index] > LIMIT) {
            return -1;
        }
        int a = seq(coins, index + 1, accumulator);
        int b = seq(coins, index + 1, accumulator + coins[index]);
        return Math.max(a, b);
    }

    private static int par(int[] coins, int index, int accumulator, Condition condicion) {
        Coin pf = new Coin(index, coins, accumulator, condicion);
        pf.fork();
        return pf.join();
    }

    public static void main(String[] args) {
        nCores = Runtime.getRuntime().availableProcessors();

        int[] coins = createRandomCoinSet(30);

        int repeats = 40;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/SequencialConditionTimes.csv"));
            StringBuilder sb = new StringBuilder("ExecutionNumber, SequencialConditionTime(ns)\n");
            writer.append(sb.toString());

            //------------------------------------------------------------
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("data/ParallelMaxLevelConditionTimes.csv"));
            StringBuilder sb1 = new StringBuilder("ExecutionNumber");
            for (int limit = 3; limit <= 30; limit++) {
                sb1.append(",ParallelMaxLevelConditionTime").append(limit).append("(ns)");
            }
            sb1.append("\n");
            writer2.write(sb1.toString());
            //------------------------------------------------------------
            BufferedWriter writer3 = new BufferedWriter(new FileWriter("data/ParallelExecutionSurplusConditionTimes.csv"));
            StringBuilder sb2 = new StringBuilder("ExecutionNumber");
            for (int limit = 0; limit <= 5; limit++) {
                sb2.append(",ParallelExecutionSurplusConditionTime").append(limit).append("(ns)");
            }
            sb2.append("\n");
            writer3.write(sb2.toString());
            //------------------------------------------------------------

            for (int i = 0; i < repeats; i++) {
                sb = new StringBuilder();
                sb.append(i);
                sb1 = new StringBuilder();
                sb1.append(i);
                sb2 = new StringBuilder();
                sb2.append(i);

                System.out.println("--------------------" + i + "--------------------");
                long seqInitialTime = System.nanoTime();
                int rs = seq(coins, 0, 0);
                long seqEndTime = System.nanoTime() - seqInitialTime;
                System.out.println(nCores + ";Sequential;" + seqEndTime);
                sb.append(",").append(seqEndTime).append("\n");
                writer.write(sb.toString());



                for (int limit = 3; limit <= 30; limit++) {

                    Condition sizeCondition = new MaxLevelCondition(limit);

                    long parInitialTimeSize = System.nanoTime();
                    int sizerp = par(coins, 0, 0, sizeCondition);
                    long parEndTimeSize = System.nanoTime() - parInitialTimeSize;
                    System.out.println(nCores + ";Parallel-MaxLevelCondition" + limit + ";" + parEndTimeSize);

                    if (sizerp != rs) {
                        System.out.println("sizerp Wrong Result!");
                        System.exit(-1);
                    }
                    sb1.append(",").append(parEndTimeSize);
                }
                sb1.append("\n");
                writer2.write(sb1.toString());


                for (int limit = 0; limit <= 5; limit++) {
                    Condition surplusCondition = new SurplusCondition(limit);

                    long parInitialTime = System.nanoTime();
                    int surplusrp = par(coins, 0, 0, surplusCondition);
                    long parEndTime = System.nanoTime() - parInitialTime;
                    System.out.println(nCores + ";Parallel-SurplusCondition" + limit + ";" + parEndTime);


                    if (surplusrp != rs) {
                        System.out.println("surplusrp Wrong Result!");
                        System.exit(-1);
                    }
                    sb2.append(",").append(parEndTime);

                }
                sb2.append("\n");
                writer3.write(sb2.toString());
            }
            writer.close();
            writer2.close();
            writer3.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}


