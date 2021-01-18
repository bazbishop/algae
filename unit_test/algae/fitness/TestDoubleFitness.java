package algae.fitness;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestDoubleFitness {

	@Test
	void testIsOptimal() {
		var notFinished = new DoubleFitness(0, false);
		assertEquals(notFinished.isOptimal(), false);

		var finished = new DoubleFitness(0, true);
		assertEquals(finished.isOptimal(), true);
	}

	@Test
	void testCompareTo() {
		var less = new DoubleFitness(-5, false);

		var middle1 = new DoubleFitness(2, false);
		var middle2 = new DoubleFitness(2, false);

		var more = new DoubleFitness(177, false);

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
		DoubleFitness a = new DoubleFitness(17, false);
		assertEquals(a.mValue, 17.0);
		assertEquals(a.isOptimal(), false);

		DoubleFitness b = new DoubleFitness(-1, true);
		assertEquals(b.mValue, -1.0);
		assertEquals(b.isOptimal(), true);
	}
}
