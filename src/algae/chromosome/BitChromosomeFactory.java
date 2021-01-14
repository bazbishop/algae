package algae.chromosome;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.util.Rand;

public class BitChromosomeFactory implements IChromosomeFactory {
	public BitChromosomeFactory( int chromosomeLength ) {
		mChromosomeLength = chromosomeLength;
	}

	public IChromosome createEmptyChromosome() {
		return new BitChromosome( mChromosomeLength );
	}

	public IChromosome createRandomChromosome() {
		final BitChromosome ch = new BitChromosome( mChromosomeLength );

		for( int a = 0; a < mChromosomeLength; ++a ) {
			if( randomAllele() )
				ch.mBits.set( a );
		}
		return ch;
	}

	public void mutateAllele( IChromosome chromosome, int index ) {
		BitChromosome ch = (BitChromosome) chromosome;

		ch.mBits.flip( index );
	}

	protected boolean randomAllele() {
		return Rand.nextBoolean();
	}

	public final int mChromosomeLength;
}
