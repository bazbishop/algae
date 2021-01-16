package algae;

/**
 * Factory to create and mutate chromosomes.
 */
public interface IChromosomeFactory {

	/**
	 * Make a fully randomised chromosome.
	 * 
	 * @return The new chromosome.
	 */
	IChromosome createRandomChromosome();

	/**
	 * Make an empty chromosome with a default value for all alleles.
	 * 
	 * @return The new chromosome.
	 */
	IChromosome createEmptyChromosome();

	/**
	 * Apply a mutation to the specified allele.
	 * 
	 * @param chromosome The chromosome to modify.
	 * @param index      The index of the allele to mutate.
	 */
	void mutateAllele(IChromosome chromosome, int index);
}
