package algae;

/**
 * Factory to create population containers.
 */
public interface IPopulationFactory {

	IPopulation createPopulation(int intendedSize);
}
