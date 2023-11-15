package knapsack;

public class ParallelLibrary {
    public static void doWorkParallel(ParallelizeWork pw, int nIterations, int nThreads) {
        Thread[] threads = new Thread[nThreads];
        for (int ti = 0; ti < nThreads; ti++) {
            final int tid = ti;
            threads[tid] = new Thread( () -> {
                int start_i = tid * nIterations / nThreads;
                int end_i = (tid+1) * nIterations / nThreads;
                if (
                        tid == nThreads-1) {
                    end_i = nIterations;
                }
                pw.runTask(start_i, end_i);
            });
            threads[tid].start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread " + t + " was interrupted");
            }
        }
    }
}
