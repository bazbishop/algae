package baz.algae.chromosome;

public interface IChromosome {
	int length();

	void copyAlleleTo( int index, IChromosome target );
}
