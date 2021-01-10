package baz.algae.chromosome.poly;

import baz.algae.IGenome;
import baz.algae.chromosome.IChromosome;

public class PolyGenome implements IGenome {
	public PolyGenome( IChromosome[] chromosomes ) {
		mChromosomes = chromosomes;
	}

	public IChromosome[] chomosomes() {
		return mChromosomes;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		for( int c = 0; c < mChromosomes.length; ++c ) {
			b.append( "[C" ).append( c ).append( ']' );
			b.append( mChromosomes[ c ].toString() );
		}
		return b.toString();
	}

	private final IChromosome[] mChromosomes;
}
