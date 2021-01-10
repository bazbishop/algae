package baz.algae.selector;

import baz.algae.ISelector;
import baz.algae.util.Random;

public class RandomSelector implements ISelector {
	/**
	 * The usual case, choose randomly twice, with no range limit for 2 parents.
	 */
	public RandomSelector() {
		mRepeat = 2;
		mMaxRange = 0;
	}

	public RandomSelector( int repeat, int range ) {
		mRepeat = repeat;
		mMaxRange = range;
	}

	public int select( int populationSize ) {
		int range = populationSize;
		if( mMaxRange > 0 )
			range = Math.min( mMaxRange, range );

		int index = range - 1;

		for( int r = 0; r < mRepeat; ++r ) {
			index = Random.nextInt( index + 1 );
		}

		return index;
	}

	private final int mMaxRange;

	private final int mRepeat;
}
