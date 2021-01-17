package algae.chromosome;

import junit.framework.TestCase;

public class TestIntegerArrayChromosome extends TestCase {

	public void testConstructor() {
		var c = new IntegerArrayChromosome(7);

		assertEquals(7, c.length());
		assertEquals(7, c.alleles().length);
	}

	public void testEquality() {

		var c1 = new IntegerArrayChromosome(7);
		var c2 = new IntegerArrayChromosome(7);
		var c3 = new IntegerArrayChromosome(7);

		for (int i = 0; i < 7; ++i) {
			c1.alleles()[i] = i + 10;
			c2.alleles()[i] = i + 10;
			c3.alleles()[i] = i + 10;
		}

		c3.alleles()[4] = -1;

		assertEquals(c1, c1);
		assertEquals(c1, c2);
		assertFalse(c1.equals(c3));
	}

	public void testCopyAllele() {

		var c1 = new IntegerArrayChromosome(7);
		var c2 = new IntegerArrayChromosome(7);

		for (int i = 0; i < 7; ++i) {
			c1.alleles()[i] = i + 10;

			c1.copyAlleleTo(i, c2);
		}

		assertEquals(c1, c2);
	}
}
