package algae.population;

import algae.IPopulation;
import algae.IPopulationFactory;

/**
 * Factory for a simple population.
 */
public class SimplePopulationFactory implements IPopulationFactory {

	@Override
	public IPopulation createPopulation(int intendedSize) {

		return new SimplePopulation(intendedSize);
	}
}
