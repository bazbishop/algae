package algae.fitness;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TestBigIntegerFitness {

	@Test
	void testIsOptimal() {
		var notFinished = new BigIntegerFitness(BigInteger.valueOf(0), false);
		assertEquals(notFinished.isOptimal(), false);

		var finished = new BigIntegerFitness(BigInteger.valueOf(0), true);
		assertEquals(finished.isOptimal(), true);
	}

	@Test
	void testCompareTo() {
		var less = new BigIntegerFitness(BigInteger.valueOf(-5), false);

		var middle1 = new BigIntegerFitness(BigInteger.valueOf(2), false);
		var middle2 = new BigIntegerFitness(BigInteger.valueOf(2), false);

		var more = new BigIntegerFitness(BigInteger.valueOf(177), false);

		assertTrue(less.compareTo(middle1) < 0);
		assertTrue(middle1.compareTo(middle1) == 0);
		assertTrue(middle1.compareTo(middle2) == 0);
		assertTrue(middle2.compareTo(more) < 0);

		assertTrue(middle1.compareTo(less) > 0);
		assertTrue(middle2.compareTo(middle1) == 0);
		assertTrue(more.compareTo(middle2) > 0);
	}

	@Test
	void testAttributes() {
		BigIntegerFitness a = new BigIntegerFitness(BigInteger.valueOf(17), false);
		assertEquals(a.mValue, BigInteger.valueOf(17));
		assertEquals(a.isOptimal(), false);

		BigIntegerFitness b = new BigIntegerFitness(BigInteger.valueOf(-1), true);
		assertEquals(b.mValue, BigInteger.valueOf(-1));
		assertEquals(b.isOptimal(), true);
	}
}
