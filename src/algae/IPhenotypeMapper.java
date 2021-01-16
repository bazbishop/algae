package algae;

/**
 * Map a genome to a phenotype.
 */
public interface IPhenotypeMapper {

	Object createPhenotype(Genome genome);
}
