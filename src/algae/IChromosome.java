package algae;

public interface IChromosome {
	int length();

	void copyAlleleTo(int index, IChromosome target);
}
