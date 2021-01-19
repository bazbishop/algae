package algae;

import java.util.List;

import algae.util.Rand;

/**
 * A life-cycle manager.
 */
public class LifeCycle {

	/**
	 * Constructor
	 * 
	 * @param parameters           The dynamic evolution parameters
	 * @param multiplicityOfGenome The number of homologous chromosomes in each
	 *                             chromosome group
	 * @param numberOfParents      The number of parents required to breed a child
	 * @param crossoverStrategy    The crossover strategy to use
	 * @param chromosomeFactories  Chromosome factories with one factory per
	 *                             homologous chromosome group
	 * @param phenotypeMapper      A factory that can transform a genome into a
	 *                             phenotype
	 * @param fitnessTester        A component to measure the fitness of a phenotype
	 */
	/*
	 * public LifeCycle(IParameters parameters, int multiplicityOfGenome, int
	 * numberOfParents, IPopulationFactory populationFactory, CrossoverStrategy
	 * crossoverStrategy, List<IChromosomeFactory> chromosomeFactories,
	 * IPhenotypeMapper phenotypeMapper, IFitnessTester fitnessTester) {
	 * this.parameters = parameters;
	 * 
	 * this.multiplicityOfGenome = multiplicityOfGenome; this.numberOfParents =
	 * numberOfParents; this.crossoverStrategy = crossoverStrategy;
	 * 
	 * this.chromosomeFactories = chromosomeFactories; this.phenotypeMapper =
	 * phenotypeMapper; this.fitnessTester = fitnessTester;
	 * 
	 * validateCrossover();
	 * 
	 * this.populationFactory = populationFactory; }
	 */
	public LifeCycle(Parameters controlParameters) {
		this.controlParameters = controlParameters;
		validateCrossover();
		mCurrentPopulation = controlParameters.getPopulationFactory()
				.createPopulation(controlParameters.getPopulationSize());
	}

	private void validateCrossover() {
		if (controlParameters.getNumberOfParents() < 1)
			throw new IllegalArgumentException("Number of parents must be >= 1");

		if (controlParameters.getGenomeMultiplicity() < 1)
			throw new IllegalArgumentException("Multiplicity of genome must be >= 1");

		if (controlParameters.getCrossoverStrategy() == CrossoverStrategy.CrossoverGametes) {
			if (controlParameters.getGenomeMultiplicity() % controlParameters.getNumberOfParents() != 0)
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
		int size = controlParameters.getPopulationSize();
		var chromosomeFactories = controlParameters.getChromosomeFactories();
		var multiplicityOfGenome = controlParameters.getGenomeMultiplicity();

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

		final var populationSize = controlParameters.getPopulationSize();
		final var elitismCount = controlParameters.getElitismCount();
		final var selector = controlParameters.getSelector();
		final var numberOfParents = controlParameters.getNumberOfParents();
		final var multiplicityOfGenome = controlParameters.getGenomeMultiplicity();
		final var chromosomeFactories = controlParameters.getChromosomeFactories();
		final var crossoverStrategy = controlParameters.getCrossoverStrategy();

		var nextGeneration = controlParameters.getPopulationFactory().createPopulation(populationSize);

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

			if (Rand.test(controlParameters.getCrossOverProbabilityPerAllele())) {
				if (count == 2)
					c = c == 0 ? 1 : 0;
				else
					c = Rand.nextNewInt(count, c);
			}

			if (Rand.test(controlParameters.getMutationProbabilityPerAllele()))
				factory.mutateAllele(result, allele);

		}

		return result;
	}

	/**
	 * Measure the fitness of all members that don't have a fitness
	 */
	private void measureFitness() {
		var phenotypeMapper = controlParameters.getPhenotypeMapper();
		var fitnessTester = controlParameters.getFitnessTester();

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

	private final Parameters controlParameters;
	/*
	 * private final IParameters parameters;
	 * 
	 * private final int multiplicityOfGenome; private final int numberOfParents;
	 * private final CrossoverStrategy crossoverStrategy;
	 * 
	 * private final IPopulationFactory populationFactory; private final
	 * List<IChromosomeFactory> chromosomeFactories; private final IPhenotypeMapper
	 * phenotypeMapper; private final IFitnessTester fitnessTester;
	 */
}
