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
			List<IChromosomeFactory> chromosomeFactories, IPhenotypeMapper phenotypeMapper,
			IFitnessTester fitnessTester) {
		this.parameters = parameters;
		this.multiplicityOfGenome = multiplicityOfGenome;
		this.numberOfParents = numberOfParents;

		assert multiplicityOfGenome % numberOfParents == 0;

		this.chromosomeFactories = chromosomeFactories;
		this.phenotypeMapper = phenotypeMapper;
		this.fitnessTester = fitnessTester;

		mCurrentPopulation = new ArrayList<Member>(parameters.populationSize());
	}

	public boolean initGeneration() {
		mFinished = false;

		createRandomPopulation();
		measureFitness();
		sort();

		return mFinished;
	}

	public boolean runGeneration() {
		breedNextGeneration();
		measureFitness();
		sort();

		return mFinished;
	}

	public List<Member> getCurrentPopulation() {
		return mCurrentPopulation;
	}

	public boolean isFinished() {
		return mFinished;
	}

	/**
	 * Create missing members randomly.
	 */
	protected void createRandomPopulation() {
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
	
	protected void breedNextGeneration() {
		
		final var populationSize = parameters.populationSize();
		final var elitismCount = parameters.elitismCount();
		final var selector = parameters.selector();
		
		List<Member> nextGeneration = new ArrayList<Member>( populationSize );

		// Elite members
		int m = 0;
		for( ; m < elitismCount; ++m )
			nextGeneration.add( mCurrentPopulation.get( m ) );

		// Bred members
		for( ; m < populationSize; ++m ) {
			final Member[] parents = new Member[ numberOfParents ];
			for( int p = 0; p < numberOfParents; ++p )
				parents[ p ] = mCurrentPopulation.get( selector.select( mCurrentPopulation.size() ) );

			final Genome[] parentGametes = makeGametes( parents );
			
			Genome child = parentGametes[0];
			for(int p = 1; p < numberOfParents; ++p) {
				child = child.combine(parentGametes[p]);
			}

			nextGeneration.add( new Member( child ) );
		}

		mCurrentPopulation = nextGeneration;
	}

	private Genome[] makeGametes(Member[] parents) {
		var result = new Genome[parents.length];
		
		for(int p = 0; p < parents.length; ++p) {
			result[p] = makeGamete(parents[p].genome());
		}
		
		return result;
	}

	public Genome makeGamete(Genome parent)
	{
		int multiplicityChild = multiplicityOfGenome / numberOfParents;

		var parentChromosomes = parent.chromosomes();
		var childChromosomes = new IChromosome[chromosomeFactories.size()][];
		int len = parentChromosomes[0][0].length();

		// Create a complete genome
		for (int f = 0; f < chromosomeFactories.size(); ++f) {
			var homologParent = parentChromosomes[f];
			var homologChild = new IChromosome[multiplicityChild];

			var factory = chromosomeFactories.get(f);
			
			for (int hc = 0; hc < multiplicityChild; ++hc) {
				int c = Rand.nextInt(homologParent.length);
				
				homologChild[hc] = factory.createEmptyChromosome();

				for( int allele = 0; allele < len; ++allele ) {
					homologParent[c].copyAlleleTo( allele, homologChild[hc] );

					if( Rand.test(parameters.crossOverProbabilityPerAllele(f))) {
						if( homologParent.length == 2 )
							c = c == 0 ? 1 : 0;
						else
							c = Rand.nextNewInt( homologParent.length, c );
					}
					
					if( Rand.test(parameters.mutationProbabilityPerAllele(f)) )
						factory.mutateAllele( homologChild[hc], allele );

				}
			}

			childChromosomes[f] = homologChild;
		}

		return new Genome(childChromosomes);
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

	private void sort() {
		Collections.sort(mCurrentPopulation, Collections.reverseOrder());
	}

	private List<Member> mCurrentPopulation;

	private boolean mFinished = false;

	private final IParameters parameters;
	private final int multiplicityOfGenome;
	private final int numberOfParents;

	private final List<IChromosomeFactory> chromosomeFactories;
	private final IPhenotypeMapper phenotypeMapper;
	private final IFitnessTester fitnessTester;
}
