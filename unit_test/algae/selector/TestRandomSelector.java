package algae.selector;

import org.junit.jupiter.api.Test;

import algae.Genome;
import algae.IChromosome;
import algae.IPopulation;
import algae.chromosome.IntegerArrayChromosomeFactory;
import algae.population.SimplePopulation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class TestRandomSelector {

	final int populationSize = 1000;
	IPopulation population;

	@BeforeEach
	void setup() {
		population = new SimplePopulation(populationSize);

		var factory = new IntegerArrayChromosomeFactory(1, 0, 1);

		for (int i = 0; i < populationSize; ++i) {
			population.addMember(new Genome(new IChromosome[][] { new IChromosome[] { factory.createRandomChromosome() } }));
		}
	}

	@Test
	void testSelect() {
		final int sectors = 10;
		final int repetitions = 10000;

		var selector = new RandomSelector();

		var counters = new int[sectors];

		for (int i = 0; i < repetitions; ++i) {
			int v = selector.select(population);
			assertTrue(v >= 0);
			assertTrue(v < population.size());

			for (int s = 0; s < sectors; ++s) {
				if (v < population.size() * (s + 1) / sectors) {
					++counters[s];
					break;
				}
			}
		}

		for (int s = 1; s < sectors; ++s) {
			assertTrue(counters[s - 1] > counters[s]);
		}
	}
}
