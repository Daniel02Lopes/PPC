package knapsack;

@FunctionalInterface
public interface ParallelizeWork {
    void runTask(int start, int end);
}
