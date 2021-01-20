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

		while (mCurrentPopulation.size() < size) {
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
		final var numberOfParents = parameters.getNumberOfParents();
		final var multiplicityOfGenome = parameters.getGenomeMultiplicity();
		final var crossoverStrategy = parameters.getCrossoverStrategy();

		final var selector = parameters.getSelector();
		final var mutator = parameters.getMutationOperator();
		final var crossover = parameters.getCrossoverOperator();
		final var chromosomeFactories = parameters.getChromosomeFactories();

		var nextGeneration = parameters.getPopulationFactory().createPopulation(populationSize);

		int numberOfSurvivors = Math.min(elitismCount, mCurrentPopulation.size());
		for (int i = 0; i < numberOfSurvivors; ++i) {
			nextGeneration.addMember(mCurrentPopulation.getMember(i));
		}

		// Breed members
		while (nextGeneration.size() < populationSize
				&& nextGeneration.discarded() < populationSize * parameters.getMaximumDiscardRatio()) {

			// === SELECTION ===
			final var parents = new Genome[numberOfParents];
			for (int p = 0; p < numberOfParents; ++p)
				parents[p] = mCurrentPopulation.getMember(selector.select(mCurrentPopulation.size()));

			// === COMBINATION ===
			Genome preChild = parents[0];
			for (int p = 1; p < numberOfParents; ++p) {
				preChild = preChild.combine(parents[p]);
			}

			// === CROSSOVER ===
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
							var input = new IChromosome[multiplicityOfGenome];
							// for(int j = p * multiplicityOfGenome; j < (p+1) * multiplicityOfGenome;++j) {
							for (int j = 0; j < multiplicityOfGenome; ++j) {
								input[j] = preChildChromosomes[homolog][p * multiplicityOfGenome + j];
							}

							var chromosome = crossover.apply(input, chromosomeFactories[homolog]);

							mutator.apply(chromosome, chromosomeFactories[homolog]);

							childHomologGroup[index++] = chromosome;
						}
					}
					break;

				case CrossoverAll:
					index = 0;
					for (int p = 0; p < multiplicityOfGenome; ++p) {
						var chromosome = crossover.apply(preChildChromosomes[homolog], chromosomeFactories[homolog]);

						mutator.apply(chromosome, chromosomeFactories[homolog]);

						childHomologGroup[index++] = chromosome;
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
