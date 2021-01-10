package baz.algae;

import java.util.List;

public class AutoLifeCycleRunner {
	public AutoLifeCycleRunner( StdLifeCycle lifeCycle, int maxGenerations ) {
		mLifeCycle = lifeCycle;
		mMaxGenerations = maxGenerations;
		mGeneration = 0;
	}

	public boolean run() {
		mLifeCycle.initGeneration();
		++mGeneration;

		while( !mLifeCycle.isFinished() && mGeneration < mMaxGenerations ) {
			mLifeCycle.runGeneration();
			++mGeneration;
			// endOfGeneration( mGeneration, mCurrentPopulation );
		}

		if( mLifeCycle.isFinished() ) {
			// finished( mGeneration, mCurrentPopulation );
			return true;
		}
		else {
			return false;
		}
	}

	/** Override for notifications of success. */
	protected void finished( int generation, List<Member> finalPopulation ) {
		System.out.println( "FINISHED!" );
	}

	protected void endOfGeneration( int generation, List<Member> finalPopulation ) {
		final Member best = mLifeCycle.getCurrentPopulation().get( 0 );
		System.out.println( "Generation: " + generation + ", best fitness: " + best.fitness + " = " + best.genome );
	}

	private final StdLifeCycle mLifeCycle;

	private final int mMaxGenerations;

	private int mGeneration;
}
