package algae.chromosome;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestIntegerArrayChromosomeFactory {

	@Test
	void testCreateEmpty() {
		var f = new IntegerArrayChromosomeFactory(17, 10, 20);

		var c = f.createEmptyChromosome();
		assertEquals(17, c.length());

		for (int i = 0; i < c.length(); ++i)
			assertEquals(0, c.alleles()[i]);

		c = f.createRandomChromosome();
		assertEquals(17, c.length());

		boolean different = false;
		int v = c.alleles()[0];
		for (int i = 0; i < c.length(); ++i) {
			int next = c.alleles()[i];
			if (next != v)
				different = true;

			v = next;

			assertTrue(v >= 10 && v <= 20);
		}

		assertTrue(different);
	}

	@Test
	void testMutateAllele() {

		var f = new IntegerArrayChromosomeFactory(17, 10, 20);

		var c = f.createEmptyChromosome();
		assertEquals(17, c.length());

		for (int i = 0; i < c.length(); ++i) {
			f.mutateAllele(c, i);
		}

		for (int i = 0; i < c.length(); ++i) {
			int v = c.alleles()[i];
			assertTrue(v >= 10 && v <= 20);
		}
	}
}
