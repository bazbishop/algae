package algae.operators.crossover;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.ICrossoverOperator;

/**
 * Null crossover operator that can only work with one parent and simply returns a copy of the parent chromosome.
 */
public class NoCrossover implements ICrossoverOperator {

	/**
	 * Constructor.
	 */
	public NoCrossover() {
	}

	@Override
	public IChromosome apply(IChromosome[] input, IChromosomeFactory factory) {

		assert input.length == 1;

		return input[0].clone();
	}
}
