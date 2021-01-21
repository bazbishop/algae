package algae.chromosome;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.util.Rand;

/**
 * A factory for creating IntegerArrayChromosome objects.
 */
public class IntegerArrayChromosomeFactory implements IChromosomeFactory {

	/**
	 * Constructor.
	 * 
	 * @param chromosomeLength The length of chromosomes to create
	 * @param minAlleleValue   The minimum allele value
	 * @param maxAlleleValue   The maximum allele value
	 */
	public IntegerArrayChromosomeFactory(int chromosomeLength, int minAlleleValue, int maxAlleleValue) {
		mChromosomeLength = chromosomeLength;
		mMinAlleleValue = minAlleleValue;
		mMaxAlleleValue = maxAlleleValue;
	}

	@Override
	public IntegerArrayChromosome createEmptyChromosome() {
		return new IntegerArrayChromosome(mChromosomeLength);
	}

	@Override
	public IntegerArrayChromosome createRandomChromosome() {
		var chromosome = new IntegerArrayChromosome(mChromosomeLength);

		for (int a = 0; a < mChromosomeLength; ++a)
			chromosome.alleles()[a] = randomAllele();

		return chromosome;
	}

	@Override
	public void mutateAllele(IChromosome chromosome, int index) {
		IntegerArrayChromosome ch = (IntegerArrayChromosome) chromosome;

		ch.alleles()[index] = randomAllele();
	}

	private int randomAllele() {
		final int range = mMaxAlleleValue - mMinAlleleValue;
		int r = Rand.nextInt(range + 1);

		return r + mMinAlleleValue;
	}

	private final int mChromosomeLength;
	private final int mMaxAlleleValue;
	private final int mMinAlleleValue;
}
