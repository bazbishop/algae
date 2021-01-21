package algae.fitness;

import algae.IFitness;

/**
 * Fitness measured using type 'int'.
 */
public class IntegerFitness implements IFitness {
	public IntegerFitness(int value, boolean finished) {
		mValue = value;
		mFinished = finished;
	}

	@Override
	public boolean isOptimal() {
		return mFinished;
	}

	@Override
	public int compareTo(IFitness o) {
		IntegerFitness rhs = (IntegerFitness) o;

		if (mValue < rhs.mValue)
			return -1;
		else if (mValue > rhs.mValue)
			return 1;
		else
			return 0;
	}

	@Override
	public boolean equals(Object o) {
		IntegerFitness rhs = (IntegerFitness) o;
		return mValue == rhs.mValue;
	}

	@Override
	public int hashCode() {
		return mValue;
	}

	@Override
	public String toString() {
		return Integer.toString(mValue);
	}

	@Override
	public Integer value() {
		return mValue;
	}

	private final int mValue;

	private final boolean mFinished;
}
