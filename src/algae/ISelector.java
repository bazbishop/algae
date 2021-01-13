package algae;

/**
 * Member selector.
 */
public interface ISelector {
	
	/**
	 * Return the index of a member from the population
	 * @param populationSize The size of the population ready for selection.
	 * @return The selected member index.
	 */
	int select( int populationSize );
}
