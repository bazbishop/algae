package algae;

/**
 * Map a genome to a phenotype.
 */
public interface IPhenotypeMapper {

	/**
	 * Transform a genome into a testable phenotype.
	 * 
	 * @param genome The genome containing genetic information
	 * @return The organism ready to be tested
	 */
	Object createPhenotype(Genome genome);
}
