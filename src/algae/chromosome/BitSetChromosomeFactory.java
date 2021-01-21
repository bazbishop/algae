package algae.chromosome;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.util.Rand;

/**
 * Factory for creating BitSetChromosome objects.
 */
public class BitSetChromosomeFactory implements IChromosomeFactory {

	/**
	 * Constructor.
	 * 
	 * @param chromosomeLength The length of the chromosome
	 */
	public BitSetChromosomeFactory(int chromosomeLength) {
		mChromosomeLength = chromosomeLength;
	}

	@Override
	public BitSetChromosome createEmptyChromosome() {
		return new BitSetChromosome(mChromosomeLength);
	}

	@Override
	public BitSetChromosome createRandomChromosome() {
		final BitSetChromosome ch = new BitSetChromosome(mChromosomeLength);

		for (int a = 0; a < mChromosomeLength; ++a) {
			if (Rand.nextBoolean())
				ch.alleles().set(a);
		}
		return ch;
	}

	@Override
	public void mutateAllele(IChromosome chromosome, int index) {
		BitSetChromosome ch = (BitSetChromosome) chromosome;

		ch.alleles().flip(index);
	}

	private final int mChromosomeLength;
}
