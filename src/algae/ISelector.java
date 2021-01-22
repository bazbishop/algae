package algae;

/**
 * Member selector.
 */
public interface ISelector {

	/**
	 * Return the index of a member from the population
	 * 
	 * @param population The population from which to select members.
	 * @return The selected member index.
	 */
	int select(IPopulation population);
}
