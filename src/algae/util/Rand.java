package algae.util;

/**
 * Random number generation and probability testing.
 */
public class Rand {

	private static MersenneTwisterFast mRandom = new MersenneTwisterFast();

	/**
	 * Get the next random boolean value.
	 * 
	 * @return The random value.
	 */
	public static boolean nextBoolean() {
		return mRandom.nextBoolean();
	}

	/**
	 * Get the next random double value.
	 * 
	 * @return The random value in the range [0.0, 1.0).
	 */
	public static double nextDouble() {
		return mRandom.nextDouble();
	}

	/**
	 * Get the next random integer value.
	 * 
	 * @param n The range limit - must be > 0
	 * @return The random value in the range [0, n-1]
	 */
	public static int nextInt(final int n) {
		assert n > 0;

		return mRandom.nextInt(n);
	}

	/**
	 * Get the next random integer value that is different to the given value.
	 * 
	 * @param n       The range limit - must be > 1
	 * @param exclude Exclude this value from the result
	 * @return The random value in the range [0, n-1]
	 */
	public static int nextNewInt(final int n, final int exclude) {
		assert n > 1;
		assert exclude < n;

		int next = nextInt(n - 1);

		if (next >= exclude)
			++next;

		return next;
	}

	/**
	 * Test for an event with the given probability.
	 * 
	 * @param probability The probability in the range [0.0, 1.0]
	 * @return true if the event occurs
	 */
	public static boolean test(double probability) {

		if (probability <= 0.0)
			return false;

		if (probability >= 1.0)
			return true;

		return nextDouble() < probability;
	}

	/**
	 * Test for an event with the given probability.
	 * 
	 * @param percentage The percentage probability of the event happening in the range [0, 100]
	 * @return
	 */
	public static boolean percent(final int percentage) {

		if (percentage <= 0)
			return false;

		if (percentage >= 100)
			return true;

		return nextInt(100) < percentage;
	}
}
