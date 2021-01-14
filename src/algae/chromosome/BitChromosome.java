package algae.chromosome;

import java.util.BitSet;

import algae.IChromosome;

public class BitChromosome implements IChromosome {
	public BitChromosome( int chromosomeLength ) {
		mChromosomeLength = chromosomeLength;
		mBits = new BitSet();
	}

	public void copyAlleleTo( int index, IChromosome target ) {
		BitChromosome rhs = (BitChromosome) target;

		rhs.mBits.set( index, mBits.get( index ) );
	}

	public int length() {
		return mChromosomeLength;
	}

	@Override
	public boolean equals( Object o ) {
		BitChromosome rhs = (BitChromosome) o;
		return mBits.equals( rhs.mBits );
	}

	@Override
	public int hashCode() {
		return mBits.hashCode();
	}

	@Override
	public String toString() {
		return mBits.toString();
	}

	public final BitSet mBits;

	public final int mChromosomeLength;
}
