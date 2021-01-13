package baz.algae;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Barry Bishop 2004
 */
public class StdLifeCycle {
	public StdLifeCycle(	int populationSize,
							IPhenotypeMapper phenotypeMapper,
							IFitnessTester tester,
							IGenomeFactory factory,
							ISelector selector ) {
		mPopulationSize = populationSize;
		mFitnessTester = tester;
		mPhenotypeMapper = phenotypeMapper;
		mGenomeFactory = factory;
		mSelector = selector;

		mCurrentPopulation = new ArrayList<Member>( populationSize );
	}
	
	public void setElitismCount( int elitismCount ) {
		if( elitismCount < 0 || elitismCount > mPopulationSize )
			throw new IllegalArgumentException( "Elitism count must be between zero and population size (inclusive)." );
		
		mElitismCount = elitismCount;
	}
	
	public void setLocalOptimiser( ILocalOptimiser optimiser, int numberOfMembersToOptimise, IGenotypeMapper genotypeMapper ) {
		if( numberOfMembersToOptimise < 0 )
			throw new IllegalArgumentException( "Number of members to optimise per generation can not be negative." );
		
		if( mNumberOfMembersToOptimise > mPopulationSize )
			throw new IllegalArgumentException( "Number of members to optimise per generation can not be greater than the population size." );
		
		mOptimiser = optimiser;
		mGenotypeMapper = genotypeMapper;
		mNumberOfMembersToOptimise = numberOfMembersToOptimise;
	}

	public static IGenome[] getParentGenomes( Member[] parents ) {
		IGenome[] parentGenomes = new IGenome[ parents.length ];

		for( int m = 0; m < parents.length; ++m )
			parentGenomes[ m ] = parents[ m ].genome;

		return parentGenomes;
	}

	/**
	 * Create missing members randomly.
	 */
	protected void createRandomPopulation() {
		for( int m = mCurrentPopulation.size(); m < mPopulationSize; ++m )
			mCurrentPopulation.add( new Member( mGenomeFactory.createRandom() ) );
	}

	public void setPopulation( List<Member> population ) {
		// mPopulationSize = population.size();

		mCurrentPopulation = new ArrayList<Member>( population );
	}

	public List<Member> getCurrentPopulation() {
		return mCurrentPopulation;
	}

	/**
	 * Measure the fitness of all members that don't have a fitness
	 */
	protected void measureFitness() {
		for( Member m : mCurrentPopulation ) {
			if( m.fitness == null ) {
				Object phenotype = mPhenotypeMapper.createPhenotype( m.genome );
				m.fitness = mFitnessTester.fitness( phenotype );
			}
			
			if( m.fitness.isFinished() )
				mFinished = true;
		}
	}
	
	protected void applyLocalOptimiser() {
		if( mOptimiser != null ) {
			boolean needToReSort = false;

			for( int m = 0; m < mNumberOfMembersToOptimise; ++m ) {
				Member member = mCurrentPopulation.get( mSelector.select(mCurrentPopulation.size() ) );
//				Member member = mCurrentPopulation.get( m );
				Object phenotype = mPhenotypeMapper.createPhenotype( member.genome );
				Object optimisedPhenotype = mOptimiser.optimise( phenotype, member.fitness );
				if( optimisedPhenotype != null ) {
					IFitness optimisedFitness = mFitnessTester.fitness(optimisedPhenotype);
					
					if( optimisedFitness.compareTo( member.fitness ) < 0 ) {
						mGenotypeMapper.updateGenome(optimisedPhenotype, member.genome);
						member.fitness = optimisedFitness;
						needToReSort = true;
					}
				}
			}
			
			// Anything to do?
			if( needToReSort ) {
				sort();
			}
		}
	}

	protected IGenome createSyntheticChild( List<Member> currentPopulation, int index ) {
		return null;
	}

	protected void breedNextGeneration() {
		final List<Member> nextGeneration = new ArrayList<Member>( mPopulationSize );

		int m = 0;
		for( ; m < mElitismCount; ++m )
			nextGeneration.add( mCurrentPopulation.get( m ) );

		for( int synth = 0; m < mPopulationSize; ++synth ) {
			final IGenome child = createSyntheticChild( mCurrentPopulation, synth );
			if( child == null )
				break;

			nextGeneration.add( new Member( child ) );
			++m;
		}

		for( ; m < mPopulationSize; ++m ) {
			int numParents = mGenomeFactory.getNumberOfParents();
			final Member[] parents = new Member[ numParents ];
			for( int p = 0; p < numParents; ++p )
				parents[ p ] = mCurrentPopulation.get( mSelector.select( mCurrentPopulation.size() ) );

			final IGenome[] parentGenomes = getParentGenomes( parents );
			final IGenome child = mGenomeFactory.breed( parentGenomes );

			nextGeneration.add( new Member( child ) );
		}

		mCurrentPopulation = nextGeneration;
	}

	protected void sort() {
		java.util.Collections.sort( mCurrentPopulation );
	}

	public void reset() {
		mCurrentPopulation.clear();

		mFinished = false;
	}

	public boolean initGeneration() {
		createRandomPopulation();
		measureFitness();
		sort();
		applyLocalOptimiser();

		return mFinished;
	}

	public boolean isFinished() {
		return mFinished;
	}

	public boolean runGeneration() {
		breedNextGeneration();
		measureFitness();
		sort();
		applyLocalOptimiser();

		return mFinished;
	}

	protected int mPopulationSize;

	protected int mElitismCount;

	protected final IFitnessTester mFitnessTester;
	
	protected IPhenotypeMapper mPhenotypeMapper;
	protected IGenotypeMapper mGenotypeMapper;

	protected final IGenomeFactory mGenomeFactory;

	protected final ISelector mSelector;
	
	protected ILocalOptimiser mOptimiser;
	
	protected int mNumberOfMembersToOptimise;

	/** Containers for members */
	protected List<Member> mCurrentPopulation;

	protected boolean mFinished = false;
}
