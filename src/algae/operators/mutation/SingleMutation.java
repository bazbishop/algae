package algae.operators.mutation;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.IMutationOperator;
import algae.util.Rand;

/**
 * Mutation operator that applies the same probability for mutation to every allele in a chromosome.
 */
public class SingleMutation implements IMutationOperator {

	/**
	 * Constructor.
	 * 
	 * @param mutationProbability The probability of a single mutation occurring in the given chromosome in the range [0.0,
	 *                            1.0]
	 */
	public SingleMutation(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	@Override
	public void apply(IChromosome chromosome, IChromosomeFactory factory) {

		// Will a mutation occur?
		if (Rand.test(mutationProbability)) {
			// Select allele
			int allele = Rand.nextInt(chromosome.length());
			factory.mutateAllele(chromosome, allele);
		}
	}

	final double mutationProbability;
}
