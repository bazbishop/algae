package algae.fitness;

import algae.IFitness;

/**
 * Fitness measured using type 'double'.
 */
public class DoubleFitness implements IFitness {
	public DoubleFitness( double value, boolean finished ) {
		mValue = value;
		mFinished = finished;
	}

	@Override
	public boolean isOptimal() {
		return mFinished;
	}

	public int compareTo( IFitness o ) {
		DoubleFitness rhs = (DoubleFitness) o;

		if( mValue < rhs.mValue )
			return -1;
		else if( mValue > rhs.mValue )
			return 1;
		else
			return 0;
	}

	@Override
	public boolean equals( Object o ) {
		DoubleFitness rhs = (DoubleFitness) o;
		return mValue == rhs.mValue;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(mValue);
	}

	@Override
	public String toString() {
		return Double.toString( mValue );
	}

	public final double mValue;

	private final boolean mFinished;
}
