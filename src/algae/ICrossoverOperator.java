package algae;

/**
 * Crossover operation to create a new chromosome from 2 or more chromosome parenets.
 */
public interface ICrossoverOperator {

	/**
	 * Perform the crossover operation.
	 * 
	 * @param input   The parent chromosomes
	 * @param factory The chromosome factory for creating an empty child chromosome
	 * @return The child chromosome
	 */
	IChromosome apply(IChromosome[] input, IChromosomeFactory factory);
}
