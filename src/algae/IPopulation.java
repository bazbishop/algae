package algae;

/**
 * Member population container.
 */
public interface IPopulation {

	/**
	 * Add a member.
	 * 
	 * @param genome The genome of the member to add
	 */
	void addMember(Genome genome);

	/**
	 * Get a member
	 * 
	 * @param memberIndex The index of the member in the population
	 * @return The genome of the specified member
	 */
	Genome getMember(int memberIndex);

	/**
	 * Set the fitness of the specified member.
	 * 
	 * @param memberIndex The index of the member in the population
	 * @param fitness     The fitness of the member
	 */
	void setFitness(int memberIndex, IFitness fitness);

	/**
	 * Get a member's fitness value
	 * 
	 * @param memberIndex The index of the member in the population
	 * @return The member's fitness
	 */
	IFitness getFitness(int memberIndex);

	/**
	 * The current number of member genomes stored.
	 * 
	 * @return The number of members
	 */
	int size();

	/**
	 * The number of discarded members.
	 * 
	 * @return The number of rejected attempts to insert a (duplicate) member
	 */
	int discarded();

	/**
	 * Sort members according to their fitness. The most fit will be stored at index
	 * zero The least fit will be stored at index size()-1.
	 */
	void sort();
}