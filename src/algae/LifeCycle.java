package algae;

import algae.util.Rand;

/**
 * A life-cycle manager.
 */
public class LifeCycle {

	/**
	 * Constructor.
	 * 
	 * @param parameters Parameters controlling the evolution process
	 */
	public LifeCycle(Parameters parameters) {
		this.parameters = parameters;
		validateCrossover();
		mCurrentPopulation = parameters.getPopulationFactory().createPopulation(parameters.getPopulationSize());
	}

	private void validateCrossover() {
		if (parameters.getNumberOfParents() < 1)
			throw new IllegalArgumentException("Number of parents must be >= 1");

		if (parameters.getGenomeMultiplicity() < 1)
			throw new IllegalArgumentException("Multiplicity of genome must be >= 1");

		if (parameters.getCrossoverStrategy() == CrossoverStrategy.CrossoverGametes) {
			if (parameters.getGenomeMultiplicity() % parameters.getNumberOfParents() != 0)
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

		++mGeneration;
		return mFinished;
	}

	/**
	 * Get the current population.
	 * 
	 * @return The list of members.
	 */
	public IPopulation getCurrentPopulation() {
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
		int size = parameters.getPopulationSize();
		var chromosomeFactories = parameters.getChromosomeFactories();
		var multiplicityOfGenome = parameters.getGenomeMultiplicity();

		for (int m = mCurrentPopulation.size(); m < size; ++m) {
			var chromosomes = new IChromosome[chromosomeFactories.length][];

			// Create a complete genome
			for (int f = 0; f < chromosomeFactories.length; ++f) {
				var homologs = new IChromosome[multiplicityOfGenome];

				for (int c = 0; c < multiplicityOfGenome; ++c) {
					homologs[c] = chromosomeFactories[f].createRandomChromosome();
				}

				chromosomes[f] = homologs;
			}

			mCurrentPopulation.addMember(new Genome(chromosomes));
		}
	}

	/**
	 * Breed the next generation using the specified crossover strategy.
	 */
	private void breedNextGeneration() {

		validateCrossover();

		final var populationSize = parameters.getPopulationSize();
		final var elitismCount = parameters.getElitismCount();
		final var selector = parameters.getSelector();
		final var numberOfParents = parameters.getNumberOfParents();
		final var multiplicityOfGenome = parameters.getGenomeMultiplicity();
		final var chromosomeFactories = parameters.getChromosomeFactories();
		final var crossoverStrategy = parameters.getCrossoverStrategy();

		var nextGeneration = parameters.getPopulationFactory().createPopulation(populationSize);

		int numberOfSurvivors = Math.min(elitismCount, mCurrentPopulation.size());
		for (int i = 0; i < numberOfSurvivors; ++i) {
			nextGeneration.addMember(mCurrentPopulation.getMember(i));
		}

		// Breed members
		while (nextGeneration.size() < populationSize) {
			final var parents = new Genome[numberOfParents];
			for (int p = 0; p < numberOfParents; ++p)
				parents[p] = mCurrentPopulation.getMember(selector.select(mCurrentPopulation.size()));

			Genome preChild = parents[0];
			for (int p = 1; p < numberOfParents; ++p) {
				preChild = preChild.combine(parents[p]);
			}

			var preChildChromosomes = preChild.chromosomes();
			assert preChildChromosomes.length == chromosomeFactories.length;

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
									p * multiplicityOfGenome, multiplicityOfGenome, chromosomeFactories[homolog],
									homolog);
						}
					}
					break;

				case CrossoverAll:
					index = 0;
					for (int p = 0; p < multiplicityOfGenome; ++p) {
						childHomologGroup[index++] = crossover(preChildChromosomes[homolog], 0,
								preChildChromosomes[homolog].length, chromosomeFactories[homolog], homolog);
					}
					break;

				default:
					throw new IllegalArgumentException("Unknown crossover strategy");
				}

				childChromosomes[homolog] = childHomologGroup;
			}

			nextGeneration.addMember(new Genome(childChromosomes));
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

			if (Rand.test(parameters.getCrossOverProbabilityPerAllele())) {
				if (count == 2)
					c = c == 0 ? 1 : 0;
				else
					c = Rand.nextNewInt(count, c);
			}

			if (Rand.test(parameters.getMutationProbabilityPerAllele()))
				factory.mutateAllele(result, allele);

		}

		return result;
	}

	/**
	 * Measure the fitness of all members that don't have a fitness
	 */
	private void measureFitness() {
		var phenotypeMapper = parameters.getPhenotypeMapper();
		var fitnessTester = parameters.getFitnessTester();

		for (int m = 0; m < mCurrentPopulation.size(); ++m) {

			if (mCurrentPopulation.getFitness(m) == null) {

				var genome = mCurrentPopulation.getMember(m);
				Object phenotype = phenotypeMapper.createPhenotype(genome);
				var fitness = fitnessTester.fitness(phenotype);

				mCurrentPopulation.setFitness(m, fitness);

				if (fitness.isOptimal())
					mFinished = true;
			}
		}

		mCurrentPopulation.sort();
	}

	private IPopulation mCurrentPopulation;

	private boolean mFinished = false;
	private int mGeneration = 0;

	private final Parameters parameters;
}
