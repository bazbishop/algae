package algae;

import algae.chromosome.IntegerArrayChromosome;
import algae.chromosome.IntegerArrayChromosomeFactory;
import algae.fitness.IntegerFitness;
import algae.operators.MultipleMutation;
import algae.population.UniquePopulationFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestLifeCycle_SimulatenousEquations {

	private IFitnessTester fitnessTester;
	private Parameters controlParameters;

	@BeforeEach
	void setUp() throws Exception {
		var chromosomeFactories = new IChromosomeFactory[] { new IntegerArrayChromosomeFactory(5, -10, 10) };

		fitnessTester = new IFitnessTester() {
			@Override
			public IFitness fitness(Object phenotype) {

				var values = (int[]) phenotype;
				int a = values[0];
				int b = values[1];
				int c = values[2];
				int d = values[3];
				int e = values[4];

				int eq0 = 3 * a + 1 * b + 4 * c + 2 * d + 2 * e;
				int eq1 = 3 * a + 4 * b + 3 * c + 0 * d + 1 * e;
				int eq2 = -3 * a - 6 * b + 5 * c + 1 * d + 2 * e;
				int eq3 = 5 * a + 1 * b + 5 * c + 6 * d + 0 * e;
				int eq4 = -1 * a + 8 * b - 2 * c + 3 * d + 8 * e;

				int diff0 = eq0 - -12;
				int diff1 = eq1 - -12;
				int diff2 = eq2 - -51;
				int diff3 = eq3 - +1;
				int diff4 = eq4 - +54;

				var sq = diff0 * diff0 + diff1 * diff1 + diff2 * diff2 + diff3 * diff3 + diff4 * diff4;

				return new IntegerFitness(-sq, sq == 0);
			}
		};

		var dummyMapper = new IPhenotypeMapper() {
			@Override
			public Object createPhenotype(Genome genome) {
				return null;
			}
		};

		controlParameters = new Parameters(chromosomeFactories, dummyMapper, fitnessTester);

		controlParameters.setPopulationSize(10000);
		controlParameters.setCrossOverProbabilityPerAllele(0.1);
		controlParameters.setMutationOperator(new MultipleMutation(0.05));
	}

	@Test
	void testMultiplicity2Parents2Gametes() {
		var phenoMapper = new IPhenotypeMapper() {

			@Override
			public Object createPhenotype(Genome genome) {

				var chr1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];
				var chr2 = (IntegerArrayChromosome) genome.chromosomes()[0][1];

				var result = new int[chr1.alleles().length];

				for (int a = 0; a < result.length; ++a) {
					result[a] = chr1.alleles()[a] + chr2.alleles()[a];
				}
				return result;
			}
		};

		controlParameters.setPhenotypeMapper(phenoMapper);
		controlParameters.setGenomeMultiplicity(2);
		controlParameters.setNumberOfParents(2);
		controlParameters.setCrossoverStrategy(CrossoverStrategy.CrossoverGametes);

		var lc = new LifeCycle(controlParameters);

		runExperiment(lc, phenoMapper);
	}

	@Test
	void testMultiplicity2Parents2All() {
		var phenoMapper = new IPhenotypeMapper() {

			@Override
			public Object createPhenotype(Genome genome) {

				var chr1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];
				var chr2 = (IntegerArrayChromosome) genome.chromosomes()[0][1];

				var result = new int[chr1.alleles().length];

				for (int a = 0; a < result.length; ++a) {
					result[a] = chr1.alleles()[a] + chr2.alleles()[a];
				}
				return result;
			}
		};

		controlParameters.setPhenotypeMapper(phenoMapper);
		controlParameters.setGenomeMultiplicity(2);
		controlParameters.setNumberOfParents(2);
		controlParameters.setCrossoverStrategy(CrossoverStrategy.CrossoverAll);

		var lc = new LifeCycle(controlParameters);

		runExperiment(lc, phenoMapper);
	}

	@Test
	void testMultiplicity1Parents2All() {

		var phenoMapper = new IPhenotypeMapper() {

			@Override
			public Object createPhenotype(Genome genome) {

				var chr1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];

				return chr1.alleles();
			}
		};

		controlParameters.setPhenotypeMapper(phenoMapper);
		controlParameters.setGenomeMultiplicity(1);
		controlParameters.setNumberOfParents(2);
		controlParameters.setCrossoverStrategy(CrossoverStrategy.CrossoverAll);

		var lc = new LifeCycle(controlParameters);

		runExperiment(lc, phenoMapper);
	}

	@Test
	void testMultiplicity1_Parents2_Gametes_Unique() {
		var phenoMapper = new IPhenotypeMapper() {

			@Override
			public Object createPhenotype(Genome genome) {

				var chr1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];
				return chr1.alleles();
			}
		};

		controlParameters.setPhenotypeMapper(phenoMapper);
		controlParameters.setGenomeMultiplicity(1);
		controlParameters.setNumberOfParents(2);
		controlParameters.setCrossoverStrategy(CrossoverStrategy.CrossoverAll);

		controlParameters.setPopulationSize(1000);
		controlParameters.setPopulationFactory(new UniquePopulationFactory());
		controlParameters.setMaximumDiscardRatio(2.0);
		controlParameters.setMutationOperator(new MultipleMutation(0.01));

		var lc = new LifeCycle(controlParameters);

		runExperiment(lc, phenoMapper);
	}

	private void runExperiment(LifeCycle lifeCycle, IPhenotypeMapper phenotypeMapper) {
		boolean finished = lifeCycle.initGeneration();

		while (!finished) {
			finished = lifeCycle.runGeneration();

			/*
			 * var size = lifeCycle.getCurrentPopulation().size(); var discarded =
			 * lifeCycle.getCurrentPopulation().discarded(); var generation =
			 * lifeCycle.generation();
			 */
			var genome = lifeCycle.getCurrentPopulation().getMember(0);
			int fitness = (int) lifeCycle.getCurrentPopulation().getFitness(0).value();

			var values = (int[]) phenotypeMapper.createPhenotype(genome);

			var a = values[0];
			var b = values[1];
			var c = values[2];
			var d = values[3];
			var e = values[4];

			System.out.println("generation: " + lifeCycle.generation() + ", a=" + a + ", b=" + b + ", c=" + c + ", d="
					+ d + ", e=" + e + " | fitness=" + fitness);

			if (lifeCycle.generation() > 1000)
				fail("Experiment is not converging.");
		}
	}
}
