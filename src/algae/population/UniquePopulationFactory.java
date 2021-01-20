package algae.population;

import algae.IPopulationFactory;

/**
 * Factory for a unique population.
 */
public class UniquePopulationFactory implements IPopulationFactory {

	@Override
	public UniquePopulation createPopulation(int intendedSize) {

		return new UniquePopulation(intendedSize);
	}
}
