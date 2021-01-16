package algae;

/**
 * A single strand of reproducible information.
 */
public interface IChromosome {

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
}
