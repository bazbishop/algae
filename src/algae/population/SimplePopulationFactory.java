package algae.population;

import algae.IPopulationFactory;

/**
 * Factory for a simple population.
 */
public class SimplePopulationFactory implements IPopulationFactory {

	@Override
	public SimplePopulation createPopulation(int intendedSize) {

		return new SimplePopulation(intendedSize);
	}
}
