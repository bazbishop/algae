package algae;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algae.chromosome.IntegerArrayChromosomeFactory;

class TestParameters {

	IChromosomeFactory[] factories;
	Parameters parameters;

	ISelector selector = new ISelector() {
		@Override
		public int select(IPopulation population) {
			return 0;
		}
	};

	IPhenotypeMapper mapper = new IPhenotypeMapper() {
		@Override
		public Object createPhenotype(Genome genome) {
			return null;
		}
	};

	IFitnessTester tester = new IFitnessTester() {
		@Override
		public IFitness fitness(Object phenotype) {
			return null;
		}
	};

	IPopulationFactory populationFactory = new IPopulationFactory() {
		@Override
		public IPopulation createPopulation(int intendedSize) {
			return null;
		}
	};

	IMutationOperator mutator = new IMutationOperator() {
		@Override
		public void apply(IChromosome chromosome, IChromosomeFactory factory) {
		}
	};

	ICrossoverOperator crossover = new ICrossoverOperator() {
		@Override
		public IChromosome apply(IChromosome[] input, IChromosomeFactory factory) {
			return null;
		}
	};

	@BeforeEach
	void setup() {
		factories = new IChromosomeFactory[] { new IntegerArrayChromosomeFactory(1, 0, 10) };

		parameters = new Parameters(factories, mapper, tester);
	}

	@Test
	void testGetSetSimple() {
		parameters.setPopulationSize(77);
		assertEquals(77, parameters.getPopulationSize());

		parameters.setPopulationFactory(populationFactory);
		assertEquals(populationFactory, parameters.getPopulationFactory());

		parameters.setElitismCount(3);
		assertEquals(3, parameters.getElitismCount());

		parameters.setMaximumDiscardRatio(8.0);
		assertEquals(8, parameters.getMaximumDiscardRatio());

		parameters.setSortPopulation(true);
		assertTrue(parameters.getSortPopulation());
		parameters.setSortPopulation(false);
		assertFalse(parameters.getSortPopulation());
		
		parameters.setSelector(selector);
		assertEquals(selector, parameters.getSelector());

		parameters.setPhenotypeMapper(mapper);
		assertEquals(mapper, parameters.getPhenotypeMapper());

		parameters.setFitnessTest(tester);
		assertEquals(tester, parameters.getFitnessTester());

		parameters.setMutationOperator(mutator);
		assertEquals(mutator, parameters.getMutationOperator());

		parameters.setCrossoverOperator(crossover);
		assertEquals(crossover, parameters.getCrossoverOperator());

		parameters.setGenomeMultiplicity(5);
		assertEquals(5, parameters.getGenomeMultiplicity());

		parameters.setNumberOfParents(7);
		assertEquals(7, parameters.getNumberOfParents());
	}

	@Test
	void testGetSetCrossover() {
		parameters.setCrossoverStrategy(CrossoverStrategy.CrossoverAll);
		assertEquals(CrossoverStrategy.CrossoverAll, parameters.getCrossoverStrategy());
		parameters.setCrossoverStrategy(CrossoverStrategy.CrossoverGametes);
		assertEquals(CrossoverStrategy.CrossoverGametes, parameters.getCrossoverStrategy());
	}

	@Test
	void testGetSetChromosomeFactories() {
		parameters.setChromosomeFactories(factories);
		assertEquals(1, parameters.getChromosomeFactories().length);
		assertEquals(factories[0], parameters.getChromosomeFactories()[0]);
	}

	@Test
	void testGetSetChromosomeFactory() {
		parameters.setChromosomeFactory(factories[0]);
		assertEquals(1, parameters.getChromosomeFactories().length);
		assertEquals(factories[0], parameters.getChromosomeFactories()[0]);
	}
}
