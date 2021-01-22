package algae;

import org.junit.jupiter.api.Test;

import algae.chromosome.IntegerArrayChromosome;
import algae.fitness.IntegerFitness;
import algae.operators.mutation.NoMutation;
import algae.population.SimplePopulationFactory;

import static org.junit.jupiter.api.Assertions.*;

public class TestLifeCycle {

	class Factory implements IChromosomeFactory {

		public Factory(int seed, boolean allowMutation) {
			this.seed = seed;
			this.allowMutation = allowMutation;
		}

		@Override
		public IChromosome createRandomChromosome() {
			var c = new IntegerArrayChromosome(len);
			for (int i = 0; i < len; ++i)
				c.alleles()[i] = seed;
			return c;
		}

		@Override
		public IChromosome createEmptyChromosome() {
			var c = new IntegerArrayChromosome(len);
			return c;
		}

		@Override
		public void mutateAllele(IChromosome chromosome, int index) {
			if (!allowMutation)
				fail("Mutation not allowed");
			var c = (IntegerArrayChromosome) chromosome;
			c.alleles()[index] *= -1;
		}

		private final int len = 10;
		private final int seed;
		private final boolean allowMutation;
	};

	class Crossover implements ICrossoverOperator {

		public Crossover(int seed) {
			this.seed = seed;
		}

		@Override
		public IChromosome apply(IChromosome[] input, IChromosomeFactory factory) {
			var c = (IntegerArrayChromosome) factory.createEmptyChromosome();
			for (int i = 0; i < c.length(); ++i)
				c.alleles()[i] = seed;
			return c;
		}

		private final int seed;
	}

	class Mapper implements IPhenotypeMapper {

		@Override
		public Integer createPhenotype(Genome genome) {
			var c = (IntegerArrayChromosome) genome.chromosomes()[0][0];
			int v = c.alleles()[0];
			return v;
		}
	}

	class Tester implements IFitnessTester {

		@Override
		public IFitness fitness(Object phenotype) {
			return new IntegerFitness((int) phenotype, false);
		}
	}

	@Test
	void testElitism() {

		// set up a population of single chromosome members, all with 3's in their
		// chromosomes and fitness
		var params = new Parameters(new Factory(3, false), new Mapper(), new Tester());

		params.setMutationOperator(new NoMutation());
		params.setCrossoverOperator(new Crossover(2));
		params.setPopulationFactory(new SimplePopulationFactory());
		params.setElitismCount(5);
		params.setPopulationSize(100);
		params.setNumberOfParents(2);
		params.setGenomeMultiplicity(1);

		var lc = new LifeCycle(params);

		int gen = 0;
		lc.initGeneration();
		assertEquals(gen++, lc.generation());

		var pop = lc.getCurrentPopulation();
		assertEquals(100, pop.size());

		for (int i = 0; i < pop.size(); ++i) {
			var g = pop.getMember(i);
			var c = (IntegerArrayChromosome) g.chromosomes()[0][0];
			var f = (IntegerFitness) pop.getFitness(i);

			assertEquals(3, c.alleles()[0]);
			assertEquals(3, f.value());
		}

		for (int j = 0; j < 1000; ++j) {

			lc.runGeneration();
			assertEquals(gen++, lc.generation());

			pop = lc.getCurrentPopulation();
			assertEquals(100, pop.size());

			for (int i = 0; i < pop.size(); ++i) {
				var g = pop.getMember(i);
				var c = (IntegerArrayChromosome) g.chromosomes()[0][0];
				var f = (IntegerFitness) pop.getFitness(i);

				if (i < 5) {
					assertEquals(3, c.alleles()[0]);
					assertEquals(3, f.value());
				} else {
					assertEquals(2, c.alleles()[0]);
					assertEquals(2, f.value());
				}
			}
		}
	}
}
