package algae.chromosome;

import junit.framework.TestCase;

public class TestIntegerArrayChromosomeFactory extends TestCase {

	public void testCreateEmpty() {
		var f = new IntegerArrayChromosomeFactory(17, 10, 20);

		var c = (IntegerArrayChromosome) f.createEmptyChromosome();
		assertEquals(17, c.length());

		for (int i = 0; i < c.length(); ++i)
			assertEquals(0, c.alleles()[i]);

		c = (IntegerArrayChromosome) f.createRandomChromosome();
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

	public void testMutateAllele() {

		var f = new IntegerArrayChromosomeFactory(17, 10, 20);

		var c = (IntegerArrayChromosome) f.createEmptyChromosome();
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
