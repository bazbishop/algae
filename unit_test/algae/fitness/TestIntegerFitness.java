package algae.fitness;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestIntegerFitness {

	@Test
	void testIsOptimal() {
		var notFinished = new IntegerFitness(0, false);
		assertEquals(notFinished.isOptimal(), false);

		var finished = new IntegerFitness(0, true);
		assertEquals(finished.isOptimal(), true);
	}

	@Test
	void testCompareTo() {
		var less = new IntegerFitness(-5, false);

		var middle1 = new IntegerFitness(2, false);
		var middle2 = new IntegerFitness(2, false);

		var more = new IntegerFitness(177, false);

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
		IntegerFitness a = new IntegerFitness(17, false);
		assertEquals(a.mValue, 17);
		assertEquals(a.isOptimal(), false);

		IntegerFitness b = new IntegerFitness(-1, true);
		assertEquals(b.mValue, -1);
		assertEquals(b.isOptimal(), true);
	}
}
