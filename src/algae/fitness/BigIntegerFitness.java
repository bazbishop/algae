package algae.fitness;

import algae.IFitness;
import java.math.BigInteger;

/**
 * Fitness measured using type 'BigInteger'.
 */
public class BigIntegerFitness implements IFitness {
	public BigIntegerFitness(BigInteger value, boolean finished) {
		mValue = value;
		mFinished = finished;
	}

	public boolean isOptimal() {
		return mFinished;
	}

	public int compareTo(IFitness o) {
		BigIntegerFitness rhs = (BigIntegerFitness) o;

		return mValue.compareTo(rhs.mValue);
	}

	@Override
	public boolean equals(Object o) {
		BigIntegerFitness rhs = (BigIntegerFitness) o;
		return mValue.equals(rhs.mValue);
	}

	@Override
	public int hashCode() {
		return mValue.hashCode();
	}

	@Override
	public String toString() {
		return mValue.toString();
	}

	public final BigInteger mValue;

	private final boolean mFinished;
}
