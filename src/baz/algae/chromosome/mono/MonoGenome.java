package baz.algae.chromosome.mono;

import baz.algae.IGenome;
import baz.algae.chromosome.IChromosome;

public class MonoGenome implements IGenome {
	public MonoGenome( IChromosome chromosome ) {
		mChromosome = chromosome;
	}

	public IChromosome chromosome() {
		return mChromosome;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append( "[C]" );
		result.append( mChromosome.toString() );
		return result.toString();
	}

	protected IChromosome mChromosome;
}
