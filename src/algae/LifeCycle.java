package algae;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algae.util.Rand;

/**
 * A life-cycle manager.
 */
public class LifeCycle {

	public LifeCycle(IParameters parameters, int multiplicityOfGenome, int numberOfParents,
			CrossoverStrategy crossoverStrategy, List<IChromosomeFactory> chromosomeFactories,
			IPhenotypeMapper phenotypeMapper, IFitnessTester fitnessTester) {
		this.parameters = parameters;

		this.multiplicityOfGenome = multiplicityOfGenome;
		this.numberOfParents = numberOfParents;
		this.crossoverStrategy = crossoverStrategy;

		this.chromosomeFactories = chromosomeFactories;
		this.phenotypeMapper = phenotypeMapper;
		this.fitnessTester = fitnessTester;

		validateCrossover();

		mCurrentPopulation = new ArrayList<Member>(parameters.populationSize());
	}

	private void validateCrossover() {
		if (numberOfParents < 1)
			throw new IllegalArgumentException("Number of parents must be >= 1");

		if (multiplicityOfGenome < 1)
			throw new IllegalArgumentException("Multiplicity of genome must be >= 1");

		if (crossoverStrategy == CrossoverStrategy.CrossoverGametes) {
			if (multiplicityOfGenome % numberOfParents != 0)
				throw new IllegalArgumentException(
						"For gamete crossover strategy, the genome multiplicity must be a multiple of the number of parents");
		}
	}

	/**
	 * Initialise the population with random members and sort them according to
	 * fitness.
	 * 
	 * @return true if an optimal member exists
	 */
	public boolean initGeneration() {
		mFinished = false;
		mGeneration = 0;

		createRandomPopulation();
		measureFitness();
		sort();

		return mFinished;
	}

	/**
	 * Breed a new generation and sort them according to fitness
	 * 
	 * @return true if an optimal member exists
	 */
	public boolean runGeneration() {
		breedNextGeneration();
		measureFitness();
		sort();

		++mGeneration;
		return mFinished;
	}

	/**
	 * Get the current population.
	 * 
	 * @return The list of members.
	 */
	public List<Member> getCurrentPopulation() {
		return mCurrentPopulation;
	}

	/**
	 * Get the value of the flag indicating whether an optimal member exists.
	 * 
	 * @return true if an optimal member exists
	 */
	public boolean isFinished() {
		return mFinished;
	}

	/**
	 * Get the generation number.
	 * 
	 * @return The generation number - 0 means initialised, but no breeding
	 */
	public int generation() {
		return mGeneration;
	}

	/**
	 * Create missing members randomly.
	 */
	private void createRandomPopulation() {
		int size = parameters.populationSize();

		for (int m = mCurrentPopulation.size(); m < size; ++m) {
			var chromosomes = new IChromosome[chromosomeFactories.size()][];

			// Create a complete genome
			for (int f = 0; f < chromosomeFactories.size(); ++f) {
				var homologs = new IChromosome[multiplicityOfGenome];

				for (int c = 0; c < multiplicityOfGenome; ++c) {
					homologs[c] = chromosomeFactories.get(f).createRandomChromosome();
				}

				chromosomes[f] = homologs;
			}

			mCurrentPopulation.add(new Member(new Genome(chromosomes)));
		}
	}

	/**
	 * 
	 */
	private void breedNextGeneration() {

		final var populationSize = parameters.populationSize();
		final var elitismCount = parameters.elitismCount();
		final var selector = parameters.selector();

		List<Member> nextGeneration = new ArrayList<Member>(populationSize);

		// Elite members
		int m = 0;
		for (; m < elitismCount; ++m)
			nextGeneration.add(mCurrentPopulation.get(m));

		// Bred members
		for (; m < populationSize; ++m) {
			final Member[] parents = new Member[numberOfParents];
			for (int p = 0; p < numberOfParents; ++p)
				parents[p] = mCurrentPopulation.get(selector.select(mCurrentPopulation.size()));

			Genome preChild = parents[0].genome();
			for (int p = 1; p < numberOfParents; ++p) {
				preChild = preChild.combine(parents[p].genome());
			}

			var preChildChromosomes = preChild.chromosomes();
			assert preChildChromosomes.length == chromosomeFactories.size();

			var childChromosomes = new IChromosome[preChildChromosomes.length][];

			for (int homolog = 0; homolog < childChromosomes.length; ++homolog) {
				var childHomologGroup = new IChromosome[multiplicityOfGenome];

				switch (crossoverStrategy) {
				case CrossoverGametes:
					int crossoversPerParent = multiplicityOfGenome / numberOfParents;

					int index = 0;
					for (int p = 0; p < numberOfParents; ++p) {
						for (int i = 0; i < crossoversPerParent; ++i) {
							childHomologGroup[index++] = crossover(preChildChromosomes[homolog],
									p * multiplicityOfGenome, multiplicityOfGenome, chromosomeFactories.get(homolog),
									homolog);
						}
					}
					break;

				case CrossoverParents:
					index = 0;
					for (int p = 0; p < multiplicityOfGenome; ++p) {
						childHomologGroup[index++] = crossover(preChildChromosomes[homolog], 0,
								preChildChromosomes[homolog].length, chromosomeFactories.get(homolog), homolog);
					}
					break;

				default:
					throw new IllegalArgumentException("Unknown crossover strategy");
				}

				childChromosomes[homolog] = childHomologGroup;
			}

			nextGeneration.add(new Member(new Genome(childChromosomes)));
		}

		mCurrentPopulation = nextGeneration;
	}

	/**
	 * Derive a chromosome from the input chromosome array using crossover and
	 * mutation.
	 * 
	 * @param input        The pool of chromosomes
	 * @param startIndex   The starting index of chromosomes considered for
	 *                     crossover
	 * @param count        The number of chromosomes considered for crossover
	 * @param factory      The relevant chromosome factory
	 * @param homologIndex The homolog index
	 * @return The child chromosome
	 */
	private IChromosome crossover(IChromosome[] input, int startIndex, int count, IChromosomeFactory factory,
			int homologIndex) {
		var result = factory.createEmptyChromosome();
		int len = result.length();

		int c = Rand.nextInt(count);

		for (int allele = 0; allele < len; ++allele) {
			input[c + startIndex].copyAlleleTo(allele, result);

			if (Rand.test(parameters.crossOverProbabilityPerAllele(homologIndex))) {
				if (count == 2)
					c = c == 0 ? 1 : 0;
				else
					c = Rand.nextNewInt(count, c);
			}

			if (Rand.test(parameters.mutationProbabilityPerAllele(homologIndex)))
				factory.mutateAllele(result, allele);

		}

		return result;
	}

	/**
	 * Measure the fitness of all members that don't have a fitness
	 */
	private void measureFitness() {
		for (int m = 0; m < mCurrentPopulation.size(); ++m) {
			var member = mCurrentPopulation.get(m);
			if (member.fitness() == null) {
				var genome = member.genome();
				Object phenotype = phenotypeMapper.createPhenotype(member.genome());
				var fitness = fitnessTester.fitness(phenotype);

				var testedMember = new Member(genome, fitness);

				mCurrentPopulation.set(m, testedMember);

				if (testedMember.fitness().isOptimal())
					mFinished = true;
			}
		}
	}

	/**
	 * Sort the members of the population according to their fitness.
	 */
	private void sort() {
		Collections.sort(mCurrentPopulation, Collections.reverseOrder());
	}

	private List<Member> mCurrentPopulation;

	private boolean mFinished = false;
	private int mGeneration = 0;

	private final IParameters parameters;

	private final int multiplicityOfGenome;
	private final int numberOfParents;
	private final CrossoverStrategy crossoverStrategy;

	private final List<IChromosomeFactory> chromosomeFactories;
	private final IPhenotypeMapper phenotypeMapper;
	private final IFitnessTester fitnessTester;
}
