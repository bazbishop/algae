package algae;

/**
 * Mutation operator applied during breeding.
 */
public interface IMutationOperator {

	/**
	 * Possibly mutate the given chromosome.
	 * 
	 * @param chromosome The chromosome that might be mutated in some way
	 * @param factory    The chromosome factory that can execute a mutation
	 */
	void apply(IChromosome chromosome, IChromosomeFactory factory);
}
