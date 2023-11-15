package src;

import java.util.concurrent.RecursiveTask;

public class MaxLevelCondition implements Condition {
	private int limit;

	public MaxLevelCondition(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean check(int coinsLength, int index, int accumulator) {
		return index >= limit;
	}
}




