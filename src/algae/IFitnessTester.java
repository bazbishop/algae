package algae;

/**
 * Measure the fitness of a phenotype.
 */
public interface IFitnessTester {

	/**
	 * Do the fitness measurement.
	 * @param phenotype The phenotype to measure
	 * @return The fitness
	 */
	IFitness fitness( Object phenotype );
}
