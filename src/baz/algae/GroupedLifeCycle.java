package baz.algae;

import java.util.ArrayList;
import java.util.List;

public class GroupedLifeCycle {
	public GroupedLifeCycle( int populationSize, int maxGenerations,
					IPhenotypeMapper mapper,
					IFitnessTester tester, IGenomeFactory factory,
	                ISelector selector, int elitismCount, int numGroups, int maxGroupCycles ) {
		mPopulationSize = populationSize;
		mMaxGenerations = maxGenerations;
		mPhenotypeMapper = mapper;
		mTester = tester;
		mFactory = factory;
		mSelector = selector;
		mElitismCount = elitismCount;
		mNumGroups = numGroups;
		mMaxGroupCycles = maxGroupCycles;

	}

	public List<Member> getBestGroupedPopulation() {
		return mBestOfGroups;
	}

	public boolean run() {
		final int numBestOfEachGroup = mPopulationSize / mNumGroups;

		List<Member> seedPopulation = new ArrayList<Member>();

		for( int groupRuns = 0; groupRuns < mMaxGroupCycles; ++groupRuns ) {
			mBestOfGroups = new ArrayList<Member>();

			for( int group = 0; group < mNumGroups; ++group ) {
				// Use this to break out earlier
				StdLifeCycle cycle = runGroup( seedPopulation );
				boolean finished = cycle.isFinished();

				// Do this in a better way
				List<Member> endPopulation = cycle.getCurrentPopulation();
				mBestOfGroups.addAll( endPopulation.subList( 0, numBestOfEachGroup ) );

				Member best = endPopulation.get( 0 );
				System.out.println( "Group: " + group + ", best fitness: " + best.fitness + " = " + best.genome );

				if( finished ) {
					return true;
				}
			}

			seedPopulation = mBestOfGroups;
		}

		return false;
	}

	private StdLifeCycle runGroup( List<Member> seedPopulation ) {
		StdLifeCycle cycle = new StdLifeCycle( mPopulationSize, mPhenotypeMapper, mTester, mFactory, mSelector );
		
		cycle.setElitismCount(mElitismCount);

		cycle.setPopulation( seedPopulation );

		AutoLifeCycleRunner runner = new AutoLifeCycleRunner( cycle, mMaxGenerations );
		runner.run();

		return cycle;
	}

	private List<Member> mBestOfGroups;

	private int mPopulationSize;

	private int mMaxGenerations;
	
	private IPhenotypeMapper mPhenotypeMapper;

	private IFitnessTester mTester;

	private IGenomeFactory mFactory;

	private ISelector mSelector;

	private int mElitismCount;

	private int mNumGroups;

	private int mMaxGroupCycles;
}
