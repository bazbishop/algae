package algae.operators;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.IMutationOperator;
import algae.util.Rand;

/**
 * Mutation operator that applies the same probability for mutation to every mutate allele in a chromosome.
 */
public class MultipleMutation implements IMutationOperator {

	/**
	 * Constructor.
	 * 
	 * @param mutationProbabilityPerAllele The probability of each allele being mutated in the range [0.0, 1.0]
	 */
	public MultipleMutation(double mutationProbabilityPerAllele) {
		this.mutationProbabilityPerAllele = mutationProbabilityPerAllele;
	}

	@Override
	public void apply(IChromosome chromosome, IChromosomeFactory factory) {

		int len = chromosome.length();
		for (int allele = 0; allele < len; ++allele) {
			if (Rand.test(mutationProbabilityPerAllele))
				factory.mutateAllele(chromosome, allele);
		}
	}

	final double mutationProbabilityPerAllele;
}
