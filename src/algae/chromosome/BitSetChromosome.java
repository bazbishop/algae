package algae.chromosome;

import java.util.BitSet;

import algae.IChromosome;

/**
 * A chromosome modelled by a BitSet
 */
public class BitSetChromosome implements IChromosome {

	/**
	 * Constructor.
	 * 
	 * @param chromosomeLength The length of the chromosome
	 */
	public BitSetChromosome(int chromosomeLength) {
		mChromosomeLength = chromosomeLength;
		mBits = new BitSet();
	}

	public void copyAlleleTo(int index, IChromosome target) {
		BitSetChromosome rhs = (BitSetChromosome) target;

		rhs.mBits.set(index, mBits.get(index));
	}

	public int length() {
		return mChromosomeLength;
	}

	@Override
	public boolean equals(Object o) {
		BitSetChromosome rhs = (BitSetChromosome) o;
		return mBits.equals(rhs.mBits);
	}

	@Override
	public int hashCode() {
		return mBits.hashCode();
	}

	@Override
	public String toString() {
		return mBits.toString();
	}

	/**
	 * Return the alleles from this chromosome.
	 * 
	 * @return The alleles
	 */
	public BitSet alleles() {
		return mBits;
	}

	private final BitSet mBits;

	private final int mChromosomeLength;
}
