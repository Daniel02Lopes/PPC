package src;

import java.util.concurrent.RecursiveTask;

public class QueuedTaskCondition implements Condition {
    private int limit;

    public QueuedTaskCondition(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean check(int coinsLength, int index, int accumulator) {
        return RecursiveTask.getQueuedTaskCount() > limit;
    }
}
