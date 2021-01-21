package algae;

import algae.operators.MultipleCrossover;
import algae.operators.MultipleMutation;
import algae.population.SimplePopulationFactory;
import algae.selector.RandomSelector;

/**
 * Parameters controlling evolution.
 */
public class Parameters {

	/**
	 * Constructor.
	 * 
	 * @param chromosomeFactories The array of chromosome factories
	 * @param phenotypeMapper     The mapper from genotype to phenotype
	 * @param fitnessTester       The fitness tester
	 */
	public Parameters(IChromosomeFactory[] chromosomeFactories, IPhenotypeMapper phenotypeMapper,
			IFitnessTester fitnessTester) {
		setChromosomeFactories(chromosomeFactories);
		setPhenotypeMapper(phenotypeMapper);
		setFitnessTest(fitnessTester);
	}

	/**
	 * Convenience constructor when only one chromosome factory is used.
	 * 
	 * @param chromosomeFactory The chromosome factory
	 * @param phenotypeMapper   The mapper from genotype to phenotype
	 * @param fitnessTester     The fitness tester
	 */
	public Parameters(IChromosomeFactory chromosomeFactory, IPhenotypeMapper phenotypeMapper,
			IFitnessTester fitnessTester) {
		var chromosomeFactories = new IChromosomeFactory[] { chromosomeFactory };
		setChromosomeFactories(chromosomeFactories);
		setPhenotypeMapper(phenotypeMapper);
		setFitnessTest(fitnessTester);
	}

	/**
	 * The desired number of members for the next generation.
	 * 
	 * @return The number of members. Must be a positive value.
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * The desired number of members for the next generation.
	 * 
	 * @param populationSize The number of members. Must be a positive value.
	 */
	public void setPopulationSize(int populationSize) {
		if (populationSize < 1)
			throw new IllegalArgumentException("Population size must be at least 1");

		this.populationSize = populationSize;
	}

	/**
	 * The factory for creating population containers.
	 * 
	 * @return The population factory
	 */
	public IPopulationFactory getPopulationFactory() {
		return populationFactory;
	}

	/**
	 * The factory for creating population containers.
	 * 
	 * @param populationFactory The population factory
	 */
	public void setPopulationFactory(IPopulationFactory populationFactory) {
		if (populationFactory == null)
			throw new IllegalArgumentException("The population factory must not be null");
		this.populationFactory = populationFactory;
	}

	/**
	 * The number of fittest members to keep at the end of each generation.
	 * 
	 * @return The number of elite members in the range [0, population]
	 */
	public int getElitismCount() {
		return elitismCount;
	}

	/**
	 * The number of fittest members to keep at the end of each generation.
	 * 
	 * @param elitismCount The number of elite members in the range [0, population]
	 */
	public void setElitismCount(int elitismCount) {
		if (elitismCount < 0)
			throw new IllegalArgumentException("Elitism count must be zero or more");
		this.elitismCount = elitismCount;
	}

	/**
	 * The maximum allowed ratio of discarded members compared to accepted members
	 * when loading a population.
	 * 
	 * @param maximumDiscardRatio The maximum discard ratio - must be non-negative
	 */
	public void setMaximumDiscardRatio(double maximumDiscardRatio) {
		if (maximumDiscardRatio < 0.0)
			throw new IllegalArgumentException("The maximum discard ratio must not be negative");
		this.maximumDiscardRatio = maximumDiscardRatio;
	}

	/**
	 * The maximum allowed ratio of discarded members compared to accepted members
	 * when loading a population.
	 * 
	 * @return The maximum discard ratio - non-negative
	 */
	public double getMaximumDiscardRatio() {
		return maximumDiscardRatio;
	}

	/**
	 * The selector for choosing the parents of the next generation.
	 * 
	 * @return The parent selector
	 */
	public ISelector getSelector() {
		return selector;
	}

	/**
	 * The selector for choosing the parents of the next generation.
	 * 
	 * @param selector The parent selector
	 */
	public void setSelector(ISelector selector) {
		this.selector = selector;
	}

	/**
	 * The mapper to transform a genotype to a phenotype for testing.
	 * 
	 * @return The phenotype mapper
	 */
	public IPhenotypeMapper getPhenotypeMapper() {
		return phenotypeMapper;
	}

	/**
	 * The mapper to transform a genotype to a phenotype for testing.
	 *
	 * @param phenotypeMapper The phenotype mapper
	 */
	public void setPhenotypeMapper(IPhenotypeMapper phenotypeMapper) {
		if (phenotypeMapper == null)
			throw new IllegalArgumentException("The phenotype mapper must not be null");
		this.phenotypeMapper = phenotypeMapper;
	}

	/**
	 * The tester to measure the fitness of a genome.
	 * 
	 * @return The fitness tester
	 */
	public IFitnessTester getFitnessTester() {
		return fitnessTester;
	}

	/**
	 * The tester to measure the fitness of a genome.
	 * 
	 * @param fitnessTester The fitness tester
	 */
	public void setFitnessTest(IFitnessTester fitnessTester) {
		if (fitnessTester == null)
			throw new IllegalArgumentException("The fitness tester must not be null");
		this.fitnessTester = fitnessTester;
	}

	/**
	 * The number of chromosomes present in each homologous group.
	 * 
	 * @param genomeMultiplicity The genome multiplicity
	 */
	public void setGenomeMultiplicity(int genomeMultiplicity) {
		if (genomeMultiplicity < 0)
			throw new IllegalArgumentException("Genome multiplicity must be one or more");
		this.genomeMultiplicity = genomeMultiplicity;
	}

	/**
	 * The number of chromosomes present in each homologous group.
	 * 
	 * @return The genome multiplicity
	 */
	public int getGenomeMultiplicity() {
		return genomeMultiplicity;
	}

	/**
	 * The number of parents required to create a child.
	 * 
	 * @return The number of parents
	 */
	public int getNumberOfParents() {
		return numberOfParents;
	}

	/**
	 * The number of parents required to create a child.
	 * 
	 * @param numberOfParents The number of parents
	 */
	public void setNumberOfParents(int numberOfParents) {
		if (numberOfParents < 1)
			throw new IllegalArgumentException("The number of parents must be one or more");
		this.numberOfParents = numberOfParents;
	}

	/**
	 * The crossover strategy applied when breeding new members of the population.
	 * 
	 * @return The crossover strategy
	 */
	public CrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}

	/**
	 * The crossover strategy applied when breeding new members of the population.
	 * 
	 * @param crossoverStrategy The crossover strategy
	 */
	public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
		this.crossoverStrategy = crossoverStrategy;
	}

	/**
	 * The factories for creating chromosomes from each homologous group.
	 * 
	 * @return The chromosome factories
	 */
	public IChromosomeFactory[] getChromosomeFactories() {
		return chromosomeFactories;
	}

	/**
	 * The factories for creating chromosomes from each homologous group.
	 * 
	 * @param chromosomeFactories The chromosome factories
	 */
	public void setChromosomeFactories(IChromosomeFactory[] chromosomeFactories) {
		this.chromosomeFactories = chromosomeFactories;
	}

	/**
	 * The factory for creating chromosomes when only one homologous group is
	 * required in the genome.
	 * 
	 * @param chromosomeFactories The chromosome factories
	 */
	public void setChromosomeFactory(IChromosomeFactory chromosomeFactory) {
		this.chromosomeFactories = new IChromosomeFactory[] { chromosomeFactory };
	}

	/**
	 * The mutation operator used during breeding.
	 * 
	 * @return The mutation operator
	 */
	public IMutationOperator getMutationOperator() {
		return mutationOperator;
	}

	/**
	 * The mutation operator used during breeding.
	 * 
	 * @param mutationOperator The mutation operator
	 */
	public void setMutationOperator(IMutationOperator mutationOperator) {
		this.mutationOperator = mutationOperator;
	}

	/**
	 * The crossover operator used during breeding.
	 * 
	 * @return The crossover operator
	 */
	public ICrossoverOperator getCrossoverOperator() {
		return crossoverOperator;
	}

	/**
	 * The crossover operator used during breeding.
	 * 
	 * @param crossoverOperator The crossover operator
	 */
	public void setCrossoverOperator(ICrossoverOperator crossoverOperator) {
		this.crossoverOperator = crossoverOperator;
	}

	// Can be defaulted

	private int populationSize = 100;
	private int genomeMultiplicity = 2;
	private int numberOfParents = 2;
	private CrossoverStrategy crossoverStrategy = CrossoverStrategy.CrossoverAll;
	private int elitismCount = 0;
	private double maximumDiscardRatio = 1.0;

	private IPopulationFactory populationFactory = new SimplePopulationFactory();
	private ISelector selector = new RandomSelector();
	private IMutationOperator mutationOperator = new MultipleMutation(0.01);
	private ICrossoverOperator crossoverOperator = new MultipleCrossover(0.02);

	// Required

	private IChromosomeFactory[] chromosomeFactories;
	private IPhenotypeMapper phenotypeMapper;
	private IFitnessTester fitnessTester;
}
