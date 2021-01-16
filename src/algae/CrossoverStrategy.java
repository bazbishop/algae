package algae;

/**
 * Specifies the crossover strategy.
 */
public enum CrossoverStrategy {

	/**
	 * Crossover occurs in a parent before combination. One child chromosome is made
	 * exactly one parent.
	 */
	CrossoverGametes,

	/**
	 * Crossover occurs after combination. One child chromosome is made from all
	 * parents.
	 */
	CrossoverAll
}
