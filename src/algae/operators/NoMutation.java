package algae.operators;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.IMutationOperator;

/**
 * Mutation operator that does not do any mutation.
 */
public class NoMutation implements IMutationOperator {

	/**
	 * Constructor.
	 */
	public NoMutation() {
	}

	@Override
	public void apply(IChromosome chromosome, IChromosomeFactory factory) {
	}
}
