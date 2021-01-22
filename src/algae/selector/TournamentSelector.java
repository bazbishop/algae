package algae.selector;

import algae.IPopulation;
import algae.ISelector;
import algae.util.Rand;

/**
 * A selector that randomly chooses N candidates from a population and selects the one with the best fitness. If the
 * population is already sorted, then the candidates selected is the one with the lowest index, otherwise the fitness is
 * checked.
 */
public class TournamentSelector implements ISelector {
	/**
	 * Constructor.
	 * @param numberOfCandidates The number of candidates randomly chosen per selection - must be > 0.
	 */
	public TournamentSelector(int numberOfCandidates) {
		this.numberOfCandidates = numberOfCandidates;
	}

	@Override
	public int select(IPopulation population) {

		final int populationSize = population.size();
		int index = Rand.nextInt(populationSize);

		for (int i = 1; i < numberOfCandidates; ++i) {

			int nextIndex = Rand.nextInt(populationSize);
			if (population.isSorted()) {
				if (nextIndex < index)
					index = nextIndex;
			} else {
				var current = population.getFitness(index);
				var next = population.getFitness(nextIndex);
				if (next.compareTo(current) > 0)
					index = nextIndex;
			}
		}

		return index;
	}

	private final int numberOfCandidates;
}
