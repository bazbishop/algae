package algae.population;

import algae.IPopulation;
import algae.IPopulationFactory;

/**
 * Factory for a unique population.
 */
public class UniquePopulationFactory implements IPopulationFactory {

	@Override
	public IPopulation createPopulation(int intendedSize) {

		return new UniquePopulation(intendedSize);
	}
}
