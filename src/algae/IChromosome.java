package algae;

/**
 * A single strand of reproducible information.
 */
public interface IChromosome extends Cloneable {

	/**
	 * The length of the chromosome, i.e. the number of alleles.
	 *
	 * @return
	 */
	int length();

	/**
	 * Copy an allele from this chromosome to another at the specified position.
	 *
	 * @param index  The index of the allele
	 * @param target The target chromosome
	 */
	void copyAlleleTo(int index, IChromosome target);

	/**
	 * Get the alleles in whatever type they are contained (array, collection, etc).
	 * @return The alleles stored in this chromosome
	 */
	Object alleles();

	/**
	 * Clone this chromosome.
	 * @return The clone
	 */
	IChromosome clone();
}
