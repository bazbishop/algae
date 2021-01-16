package algae.selector;

import algae.ISelector;
import algae.util.Rand;

/**
 * A selector that biases towards fitter members, by selecting randomly and
 * using the generated number to define a subset from which the next random
 * number is generated.
 */
public class RandomSelector implements ISelector {
	/**
	 * Default constructor - choose randomly twice.
	 */
	public RandomSelector() {
		mRepeat = 2;
	}

	/**
	 * Constructor - select repeatedly from a subset of the given range
	 * 
	 * @param repeat
	 */
	public RandomSelector(int repeat) {
		mRepeat = repeat;
	}

	/**
	 * Perform the selection on the given range.
	 */
	public int select(int range) {
		int index = range - 1;

		for (int r = 0; r < mRepeat; ++r) {
			index = Rand.nextInt(index + 1);
		}

		return index;
	}

	private final int mRepeat;
}
