package algae.chromosome;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestBitSetChromosomeFactory {

	@Test
	void testCreateEmpty() {
		var f = new BitSetChromosomeFactory(17);

		var c = (BitSetChromosome) f.createEmptyChromosome();
		assertEquals(17, c.length());

		for (int i = 0; i < c.length(); ++i)
			assertFalse(c.alleles().get(i));

		c = (BitSetChromosome) f.createRandomChromosome();
		assertEquals(17, c.length());

		boolean different = false;
		boolean v = c.alleles().get(0);
		for (int i = 0; i < c.length(); ++i) {
			boolean next = c.alleles().get(i);
			if (next != v)
				different = true;

			v = next;
		}

		assertTrue(different);
	}

	@Test
	void testMutateAllele() {

		var f = new BitSetChromosomeFactory(50);

		var c = (BitSetChromosome) f.createEmptyChromosome();
		assertEquals(50, c.length());

		for (int i = 0; i < c.length(); ++i) {
			f.mutateAllele(c, i);
		}

		int countSetBits = 0;
		for (int i = 0; i < c.length(); ++i) {
			if (c.alleles().get(i))
				++countSetBits;
		}
		assertEquals(50, countSetBits);

		c = (BitSetChromosome) f.createRandomChromosome();
		countSetBits = 0;
		for (int i = 0; i < c.length(); ++i) {
			if (c.alleles().get(i))
				++countSetBits;
		}

		for (int i = 0; i < c.length(); ++i) {
			f.mutateAllele(c, i);
		}

		int countUnsetBits = 0;
		for (int i = 0; i < c.length(); ++i) {
			if (!c.alleles().get(i))
				++countUnsetBits;
		}

		assertEquals(countSetBits, countUnsetBits);
	}
}
