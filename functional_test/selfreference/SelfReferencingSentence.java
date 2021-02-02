package selfreference;

import algae.CrossoverStrategy;
import algae.Genome;
import algae.IChromosomeFactory;
import algae.IFitness;
import algae.IFitnessTester;
import algae.IPhenotypeMapper;
import algae.LifeCycle;
import algae.Parameters;
import algae.chromosome.IntegerArrayChromosome;
import algae.chromosome.IntegerArrayChromosomeFactory;
import algae.fitness.IntegerFitness;
import algae.operators.crossover.MultipleCrossover;
import algae.operators.mutation.MultipleMutation;
import algae.population.SimplePopulationFactory;

/**
 * Compute solutions to the 'self-counting' sentence problem.
 */
public class SelfReferencingSentence {

	/**
	 * Off the shelf chromosome factory. If the phenotype mapper derives counter values by summing allele values from both
	 * chromosomes then the range of the allele values can be reduced.
	 */
	private static final IChromosomeFactory chromosomeFactory = new IntegerArrayChromosomeFactory(26, 1, 40);
	// private static final IChromosomeFactory chromosomeFactory = new IntegerArrayChromosomeFactory(16, 0, 21);

	/**
	 * Anonymous phenotype mapper.
	 */
	private static final IPhenotypeMapper phenotypeMapper = new IPhenotypeMapper() {

		private final NumberToWords converter = new NumberToWords(NumberToWords.Style.BRITISH);

		/**
		 * The phenotype consists of the derived values of the counters for each letter as well as the constructed sentence.
		 * Note that 10 of the letters are constant, since these letters do not occur in any of the numbers likely to occur in
		 * the finished sentence.
		 */
		@Override
		public Object createPhenotype(Genome genome) {

			var chromosome1 = (IntegerArrayChromosome) genome.chromosomes()[0][0];
			var counts1 = chromosome1.alleles();
			var chromosome2 = (IntegerArrayChromosome) genome.chromosomes()[0][1];
			var counts2 = chromosome2.alleles();

			var counts = new int[26];
			var result = new StringBuilder();

			result.append("this sentence has ");

			for (int i = 0; i < 26; ++i) {

				int v = 0;

				switch (i) {
				case 0:
					v = 3;
					break;
				case 1:
					v = 1;
					break;
				case 2:
					v = 3;
					break;
				case 3:
					v = 2;
					break;
				case 4:
					v = computeCounter(counts1, counts2, 0);
					break;
				case 5:
					v = computeCounter(counts1, counts2, 1);
					break;
				case 6:
					v = computeCounter(counts1, counts2, 2);
					break;
				case 7:
					v = computeCounter(counts1, counts2, 3);
					break;
				case 8:
					v = computeCounter(counts1, counts2, 4);
					break;
				case 9:
					v = 1;
					break;
				case 10:
					v = 1;
					break;
				case 11:
					v = computeCounter(counts1, counts2, 5);
					break;
				case 12:
					v = 1;
					break;
				case 13:
					v = computeCounter(counts1, counts2, 6);
					break;
				case 14:
					v = computeCounter(counts1, counts2, 7);
					break;
				case 15:
					v = 1;
					break;
				case 16:
					v = 1;
					break;
				case 17:
					v = computeCounter(counts1, counts2, 8);
					break;
				case 18:
					v = computeCounter(counts1, counts2, 9);
					break;
				case 19:
					v = computeCounter(counts1, counts2, 10);
					break;
				case 20:
					v = computeCounter(counts1, counts2, 11);
					break;
				case 21:
					v = computeCounter(counts1, counts2, 12);
					break;
				case 22:
					v = computeCounter(counts1, counts2, 13);
					break;
				case 23:
					v = computeCounter(counts1, counts2, 14);
					break;
				case 24:
					v = computeCounter(counts1, counts2, 15);
					break;
				case 25:
					v = 1;
					break;
				}

				counts[i] = v;

				result.append(converter.convert(counts[i])).append(" '").append((char) ('a' + i)).append("'");

				if (counts[i] != 1)
					result.append('s');

				if (i < 24)
					result.append(", ");
				else if (i == 24)
					result.append(" and ");
			}

			return new Object[] { result.toString(), counts };
		}

		/**
		 * Derive a counter value from the alleles from two chromosomes.
		 */
		int computeCounter(int[] counts1, int[] counts2, int i) {

			// Left, i.e. use just one value and ignore the other.
			return counts1[i];

			// Diff
			// int v = counts1[i] - counts2[i];
			// v = Math.abs(v);
			// return v;

			// Min
			// return Math.min(counts1[i], counts2[i]);

			// average
			// return (counts1[i] + counts2[i]) / 2;

			// mod
			// return counts1[i] % counts2[i];

			// Sum
			// return counts1[i] + counts2[i] + 1;
		}
	};

	/**
	 * Anonymous fitness tester.
	 */
	private static IFitnessTester fitnessTester = new IFitnessTester() {

		/**
		 * Measure the error resulting from differences in the 'guessed' letter counts and the actual count of letters in the
		 * complete sentence.
		 */
		@Override
		public IFitness fitness(Object phenotype) {

			var items = (Object[]) phenotype;
			var sentence = (String) items[0];
			var expectedCount = (int[]) (items[1]);

			var actualCounts = countLetters(sentence);

			int error = 0;

			for (int i = 0; i < 26; ++i) {
				int delta = actualCounts[i] - expectedCount[i];

				// error += Math.abs(delta); // Linear sum
				error += delta * delta; // Least squares
			}

			return new IntegerFitness(-error, error == 0);
		}

		private int[] countLetters(String sentence) {

			var result = new int[26];

			for (int i = 0; i < sentence.length(); ++i) {
				char ch = sentence.charAt(i);

				int letter = ch - 'a';

				if (letter >= 0 && letter < 26) {
					++result[letter];
				}
			}

			return result;
		}
	};

	private static double mutation = 1.0 / 256.0;

	public static void main(String[] args) {

		var parameters = new Parameters(chromosomeFactory, phenotypeMapper, fitnessTester);
		parameters.setPopulationSize(10_000_000);
		parameters.setGenomeMultiplicity(2);
		parameters.setCrossoverStrategy(CrossoverStrategy.CrossoverGametes);
		parameters.setNumberOfParents(2);
		parameters.setElitismCount(0);
		// parameters.setSelector(new TournamentSelector(2));
		parameters.setCrossoverOperator(new MultipleCrossover(0.08));
		parameters.setMutationOperator(new MultipleMutation(mutation));
		parameters.setPopulationFactory(new SimplePopulationFactory());
		parameters.setMaximumDiscardRatio(1.0);

		var lifeCycle = new LifeCycle(parameters);

		boolean finished = lifeCycle.initGeneration();

		status(lifeCycle);

		while (!finished) {
			finished = lifeCycle.runGeneration();

			status(lifeCycle);

			/*
			 * Experiment changing the mutation probability to avoid the population becoming homogeneous. This requires using the
			 * UniquePopulation container type.
			 * 
			 * var population = lifeCycle.getCurrentPopulation();
			 * 
			 * if(1.0 * population.discarded() / population.size() > 0.005) { parameters.setElitismCount(0); //mutation *=.0;
			 * //mutation = Math.min(mutation, 0.2); mutation = 0.2; } else {// if(population.discarded() < 200 && mutation >
			 * 0.0001) { parameters.setElitismCount(0); mutation *= 0.5; mutation = Math.max(mutation, 0.001); }
			 * 
			 * parameters.setMutationOperator(new MultipleMutation(mutation));
			 */
		}
		System.out.println();
	}

	private static String previousSentence = "";

	private static void status(LifeCycle lifeCycle) {
		var population = lifeCycle.getCurrentPopulation();

		System.out.println("Generation: " + lifeCycle.generation());
		System.out.println("Population: " + population.size() + "  (discarded: " + population.discarded() + ", mutation: " + mutation + ")");
		System.out.println("Fitness:    " + population.getFitness(0));

		var bestGenome = population.getMember(0);

		var phenotypeParts = (Object[]) phenotypeMapper.createPhenotype(bestGenome);

		int[] counts = (int[]) phenotypeParts[1];
		var s = new StringBuilder();
		for (int c = 0; c < counts.length; ++c) {
			if (c > 0)
				s.append(':');
			s.append(counts[c]);
		}

		System.out.println("Counters:   " + s.toString());

		var sentence = (String) phenotypeParts[0];
		if (!sentence.equals(previousSentence)) {
			System.out.println(sentence);
			previousSentence = sentence;
		}
	}
}
