package algae;

/**
 * Dynamic parameters controlling the evolution of a generation.
 */
public interface IParameters {

	/**
	 * The desired number of members for the next generation.
	 * @return The number of members. Must be a positive value.
	 */
	int populationSize();
	
	/**
	 * The number of fittest members to keep at the end of each generation.
	 * @return The number of elite members in the range [0, population]
	 */
	int elitismCount();
	
	/**
	 * The crossover probability per allele for the given chromosome set.
	 * This value is checked at the beginning of each breeding cycle. 
	 * @param chromosomeSetIndex The zero-based chromosome set index.
	 * @return The probability in the range [0.0, 1.0]
	 */
	double crossOverProbabilityPerAllele(int chromosomeSetIndex);
	
	/**
	 * The mutation probability per allele for the given chromosome set.
	 * This value is checked at the beginning of each breeding cycle. 
	 * @param chromosomeSetIndex The zero-based chromosome set index.
	 * @return The probability in the range [0.0, 1.0]
	 */
	double mutationProbabilityPerAllele(int chromosomeSetIndex);
	
	/**
	 * Provide a selector for the next generation.
	 * @return
	 */
	ISelector selector();
}
