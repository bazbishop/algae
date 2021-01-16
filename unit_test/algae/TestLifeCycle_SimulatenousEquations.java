package algae;

import java.util.ArrayList;
import java.util.List;

import algae.chromosome.IntegerArrayChromosome;
import algae.chromosome.IntegerArrayChromosomeFactory;
import algae.fitness.IntegerFitness;
import algae.selector.RandomSelector;
import junit.framework.TestCase;

public class TestLifeCycle_SimulatenousEquations extends TestCase {

	private List<IChromosomeFactory> chromosomeFactories;
	private IParameters parameters;
	private IFitnessTester fitnessTester;

	@Override
	protected void setUp() throws Exception {
		var chromosomeFactory = new IntegerArrayChromosomeFactory(5, -10, 10);
		chromosomeFactories = new ArrayList<IChromosomeFactory>();
		chromosomeFactories.add(chromosomeFactory);

		parameters = new IParameters() {

			@Override
			public int populationSize() {
				return 10000;
			}

			@Override
			public int elitismCount() {
				return 0;
			}

			@Override
			public double crossOverProbabilityPerAllele(int chromosomeSetIndex) {
				return .1;
			}

			@Override
			public double mutationProbabilityPerAllele(int chromosomeSetIndex) {
				return .05;
			}

			@Override
			public ISelector selector() {
				return new RandomSelector();
			}
		};

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

	}

	public void testMultiplicity2Parents2Gametes() {
		var phenoMapper = new IPhenotypeMapper() {

			@Override
			public Object createPhenotype(Genome genome) {

				var chr1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];
				var chr2 = (IntegerArrayChromosome) genome.chromosomes()[0][1];

				var result = new int[chr1.mAlleles.length];

				for (int a = 0; a < result.length; ++a) {
					result[a] = chr1.mAlleles[a] + chr2.mAlleles[a];
				}
				return result;

			}

		};

		var lc = new LifeCycle(parameters, 2, 2, CrossoverStrategy.CrossoverGametes, chromosomeFactories, phenoMapper,
				fitnessTester);

		runExperiment(lc, phenoMapper);
	}

	public void testMultiplicity2Parents2All() {
		var phenoMapper = new IPhenotypeMapper() {

			@Override
			public Object createPhenotype(Genome genome) {

				var chr1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];
				var chr2 = (IntegerArrayChromosome) genome.chromosomes()[0][1];

				var result = new int[chr1.mAlleles.length];

				for (int a = 0; a < result.length; ++a) {
					result[a] = chr1.mAlleles[a] + chr2.mAlleles[a];
				}
				return result;

			}

		};

		var lc = new LifeCycle(parameters, 2, 2, CrossoverStrategy.CrossoverAll, chromosomeFactories, phenoMapper,
				fitnessTester);

		runExperiment(lc, phenoMapper);
	}

	public void testMultiplicity1Parents2All() {

		var phenoMapper = new IPhenotypeMapper() {

			@Override
			public Object createPhenotype(Genome genome) {

				var chr1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];

				return chr1.mAlleles;
			}

		};

		var lc = new LifeCycle(parameters, 1, 2, CrossoverStrategy.CrossoverAll, chromosomeFactories, phenoMapper,
				fitnessTester);

		runExperiment(lc, phenoMapper);
	}

	private void runExperiment(LifeCycle lifeCycle, IPhenotypeMapper phenotypeMapper) {
		boolean finished = lifeCycle.initGeneration();

		while (!finished) {
			finished = lifeCycle.runGeneration();

			var best = lifeCycle.getCurrentPopulation().get(0);
			int fitness = ((IntegerFitness) best.fitness()).mValue;

			var values = (int[]) phenotypeMapper.createPhenotype(best.genome());

			var a = values[0];
			var b = values[1];
			var c = values[2];
			var d = values[3];
			var e = values[4];

			System.out.println("a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + " | fitness=" + fitness);

			if (lifeCycle.generation() > 1000)
				fail("Experiment is not converging.");
		}
	}
}
