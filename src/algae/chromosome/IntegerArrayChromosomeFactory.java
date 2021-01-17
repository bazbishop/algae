package algae.chromosome;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.util.Rand;

public class IntegerArrayChromosomeFactory implements IChromosomeFactory {
	public IntegerArrayChromosomeFactory( int chromosomeLength, int minAlleleValue, int maxAlleleValue ) {
		mChromosomeLength = chromosomeLength;
		mMinAlleleValue = minAlleleValue;
		mMaxAlleleValue = maxAlleleValue;
	}

	public IChromosome createEmptyChromosome() {
		return new IntegerArrayChromosome( mChromosomeLength );
	}

	public IChromosome createRandomChromosome() {
		var chromosome = new IntegerArrayChromosome( mChromosomeLength);
		
		for( int a = 0; a < mChromosomeLength; ++a )
			chromosome.alleles()[ a ] = randomAllele();

		return chromosome;
	}

	public void mutateAllele( IChromosome chromosome, int index ) {
		IntegerArrayChromosome ch = (IntegerArrayChromosome) chromosome;

		ch.alleles()[ index ] = randomAllele();
	}

	protected int randomAllele() {
		final int range = mMaxAlleleValue - mMinAlleleValue;
		int r = Rand.nextInt( range + 1 );

		return r + mMinAlleleValue;
	}

	protected final int mChromosomeLength;

	protected final int mMaxAlleleValue;

	protected final int mMinAlleleValue;
}
