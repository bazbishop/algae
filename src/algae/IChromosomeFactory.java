package algae;

/**
 * Factory to create and mutate chromosomes.
 */
public interface IChromosomeFactory {
	IChromosome createRandomChromosome();

	IChromosome createEmptyChromosome();

	void mutateAllele( IChromosome chromosome, int index );
}
