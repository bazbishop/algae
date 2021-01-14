package algae.util;

public class Rand {
	// private static final java.util.Random rand = new java.util.Random(
	// System.currentTimeMillis() );
	private static MersenneTwisterFast mRandom = new MersenneTwisterFast();

	public static boolean nextBoolean() {
		return mRandom.nextBoolean();
	}

	public static int nextInt( final int top ) {
		assert top > 0;

		return mRandom.nextInt( top );
	}

	public static int nextNewInt( final int top, final int lastValue ) {
		assert top > 1;
		assert lastValue < top;

		int next = nextInt( top - 1 );

		if( next >= lastValue )
			++next;

		return next;
	}

	public static boolean percent( final int percentage ) {
		return nextInt( 100 ) < percentage;
	}

	public static double nextDouble() {
		return mRandom.nextDouble();
	}
	
	public static boolean test(double probability) {
		if(probability <= 0.0)
			return false;

		if(probability >= 1.0)
			return true;
		
		return nextDouble() < probability;
	}
}
