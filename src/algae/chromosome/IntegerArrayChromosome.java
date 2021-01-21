package algae.chromosome;

import java.util.Arrays;

import algae.IChromosome;

/**
 * Chromosome modelled on an integer array.
 */
public class IntegerArrayChromosome implements IChromosome {

	/**
	 * Constructor.
	 * 
	 * @param length The length of the chromosome
	 */
	public IntegerArrayChromosome(int length) {
		mAlleles = new int[length];
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < mAlleles.length; ++i) {
			if (i > 0)
				result.append(':');
			result.append(mAlleles[i]);
		}
		return result.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		IntegerArrayChromosome rhs = (IntegerArrayChromosome) o;
		return Arrays.equals(mAlleles, rhs.mAlleles);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(mAlleles);
	}

	@Override
	public void copyAlleleTo(int index, IChromosome target) {
		IntegerArrayChromosome rhs = (IntegerArrayChromosome) target;

		rhs.mAlleles[index] = mAlleles[index];
	}

	@Override
	public int length() {
		return mAlleles.length;
	}

	/**
	 * Get the alleles.
	 * 
	 * @return The alleles as an integer array
	 */
	@Override
	public int[] alleles() {
		return mAlleles;
	}

	private final int[] mAlleles;
}
