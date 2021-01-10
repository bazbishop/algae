package baz.algae.chromosome;

import baz.algae.util.Random;

public class IntegerChromosomeFactory implements IChromosomeFactory {
	public IntegerChromosomeFactory( int chromosomeLength, int maxAlleleValue, int minAlleleValue ) {
		mChromosomeLength = chromosomeLength;
		mMaxAlleleValue = maxAlleleValue;
		mMinAlleleValue = minAlleleValue;
	}

	public IChromosome createEmptyChromosome() {
		return new IntegerChromosome( mChromosomeLength );
	}

	public IChromosome createRandomChromosome() {
		final IntegerChromosome ch = new IntegerChromosome( mChromosomeLength );

		for( int a = 0; a < mChromosomeLength; ++a )
			ch.mAlleles[ a ] = randomAllele();

		return ch;
	}

	public void mutateAllele( IChromosome chromosome, int index ) {
		IntegerChromosome ch = (IntegerChromosome) chromosome;

		ch.mAlleles[ index ] = randomAllele();
	}

	protected int randomAllele() {
		final int range = mMaxAlleleValue - mMinAlleleValue;
		int r = Random.nextInt( range + 1 );

		return r + mMinAlleleValue;
	}

	protected final int mChromosomeLength;

	protected final int mMaxAlleleValue;

	protected final int mMinAlleleValue;
}
