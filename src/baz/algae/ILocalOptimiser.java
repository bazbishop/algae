package baz.algae;

/**
 * A local optimiser is applied to a genome after testing for fitness.
 * The idea is that a selection of the best fitness members will undergo
 * some local optimisation step that will modify their genome.
 * Local optimisation is assumed to be computationally expensive.
 */
public interface ILocalOptimiser {
	
	/**
	 * Attempt an optimisation. If no improvement is found then return null.
	 * The fitness of the returned phenotype (if any) will be measured and
	 * if an improvement is detected then it will be mapped back in to a genome
	 * and added to the gene pool. 
	 * @param phenotype The phenotype of the individual to be optimised.
	 * @param fitness The fitness measure of the genome prior to optimisation.
	 * @return An optimised phenotype, or null if no optimisation is found.
	 */
	Object optimise( Object phenotype, IFitness fitness );
}
