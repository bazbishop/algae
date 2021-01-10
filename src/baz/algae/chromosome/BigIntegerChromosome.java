package baz.algae.chromosome;

import java.math.BigInteger;

public class BigIntegerChromosome implements IChromosome {
	public BigIntegerChromosome( int chromosomeLength ) {
		mChromosomeLength = chromosomeLength;
		mValue = BigInteger.ZERO;
	}

	public void copyAlleleTo( int index, IChromosome target ) {
		final BigIntegerChromosome rhs = (BigIntegerChromosome) target;

		rhs.mValue = mValue.testBit( index ) ? rhs.mValue.setBit( index ) : rhs.mValue.clearBit( index );
	}

	public int length() {
		return mChromosomeLength;
	}

	@Override
	public boolean equals( Object o ) {
		final BigIntegerChromosome rhs = (BigIntegerChromosome) o;
		return mValue.equals( rhs.mValue );
	}

	@Override
	public int hashCode() {
		return mValue.hashCode();
	}

	@Override
	public String toString() {
		return mValue.toString();
	}

	public BigInteger mValue;

	public final int mChromosomeLength;
}
