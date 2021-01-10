package baz.algae.chromosome;

public interface IChromosomeFactory {
	IChromosome createRandomChromosome();

	IChromosome createEmptyChromosome();

	void mutateAllele( IChromosome chromosome, int index );
}
