package algae.util;

import junit.framework.TestCase;

public class TestRand extends TestCase {

	public void testNextBoolean() {
		boolean falseReturned = false;
		boolean trueReturned = false;

		for (int i = 0; i < 100; ++i) {
			if (Rand.nextBoolean())
				trueReturned = true;
			else
				falseReturned = true;
		}

		assertTrue(falseReturned);
		assertTrue(trueReturned);
	}

	public void testNextDouble() {
		final int sectors = 4;
		final int repetitions = 1000;
		var counters = new int[sectors];

		for (int i = 0; i < repetitions; ++i) {
			double v = Rand.nextDouble();
			assertTrue(v >= 0.0);
			assertTrue(v < 1.0);

			for (int s = 0; s < sectors; ++s) {
				if (v < 1.0 * (s + 1) / sectors) {
					++counters[s];
					break;
				}
			}
		}

		for (int s = 0; s < sectors; ++s) {
			assertTrue(counters[s] > 0.75 * repetitions / sectors);
		}
	}

	public void testNextInt() {
		final int sectors = 4;
		final int repetitions = 1000;
		final int range = 1000;

		var counters = new int[sectors];

		for (int i = 0; i < repetitions; ++i) {
			int v = Rand.nextInt(range);
			assertTrue(v >= 0);
			assertTrue(v < range);

			for (int s = 0; s < sectors; ++s) {
				if (v < range * (s + 1) / sectors) {
					++counters[s];
					break;
				}
			}
		}

		for (int s = 0; s < sectors; ++s) {
			assertTrue(counters[s] > 0.75 * repetitions / sectors);
		}
	}

	public void testNextNewInt() {
		final int repetitions = 1000;
		final int range = 100;

		int previous = 0;

		for (int i = 0; i < repetitions; ++i) {
			int v = Rand.nextNewInt(range, previous);
			assertTrue(v >= 0);
			assertTrue(v < range);
			assertTrue(v != previous);

			previous = v;
		}
	}

	public void testPercent() {

		for (int probability = 0; probability <= 100; ++probability) {
			final int repetitions = 10000;
			int count = 0;
			for (int i = 0; i < repetitions; ++i) {
				if (Rand.percent(probability))
					++count;
			}

			double actual = 1.0 * count / repetitions;
			assertEquals(probability / 100.0, actual, 0.02);
		}
	}

	public void testTest() {

		for (double probability = 0.125; probability < 1.0; probability += 0.125) {
			final int repetitions = 10000;
			int count = 0;
			for (int i = 0; i < repetitions; ++i) {
				if (Rand.test(probability))
					++count;
			}

			double actual = 1.0 * count / repetitions;
			assertEquals(probability, actual, 0.02);
		}
	}
}
