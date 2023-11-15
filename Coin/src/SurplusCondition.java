package src;

import java.util.concurrent.RecursiveTask;


public class SurplusCondition implements Condition {
	private int limit;

	public SurplusCondition(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean check(int coinsLength, int index, int accumulator) {
		return RecursiveTask.getSurplusQueuedTaskCount() > limit;
	}
}
