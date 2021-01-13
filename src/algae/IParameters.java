package algae;

/**
 * Parameters controlling evolution.
 */
public interface IParameters {

	int populationSize();
	
	int ElitismCount();
	
	double crossOverProbabilityPerAllele(int chromosomeSetIndex);
	
	double mutationProbabilityPerAllele(int chromosomeSetIndex);
	
	/**
	 * Provide a selector for the next generation.
	 * @return
	 */
	ISelector selector();
}
