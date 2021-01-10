package baz.algae.fitness;

import baz.algae.IFitness;

public class IntegerFitness implements IFitness {
	public IntegerFitness( int value, boolean finished ) {
		mValue = value;
		mFinished = finished;
	}

	public boolean isFinished() {
		return mFinished;
	}

	public int compareTo( IFitness o ) {
		IntegerFitness rhs = (IntegerFitness) o;

		if( mValue < rhs.mValue )
			return -1;
		else if( mValue > rhs.mValue )
			return 1;
		else
			return 0;
	}

	@Override
	public boolean equals( Object o ) {
		IntegerFitness rhs = (IntegerFitness) o;
		return mValue == rhs.mValue;
	}

	@Override
	public int hashCode() {
		return mValue;
	}

	@Override
	public String toString() {
		return Integer.toString( mValue );
	}

	public final int mValue;

	public final boolean mFinished;
}
