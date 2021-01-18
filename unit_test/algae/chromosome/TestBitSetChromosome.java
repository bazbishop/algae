package algae.chromosome;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestBitSetChromosome {

	@Test
	void testConstructor() {
		var c = new BitSetChromosome(7);

		assertEquals(7, c.length());
	}

	@Test
	void testEquality() {

		var c1 = new BitSetChromosome(7);
		var c2 = new BitSetChromosome(7);
		var c3 = new BitSetChromosome(7);

		for (int i = 0; i < 7; ++i) {
			c1.alleles().set(i, i % 2 == 0);
			c2.alleles().set(i, i % 2 == 0);
			c3.alleles().set(i, i % 2 == 0);
		}

		c3.alleles().flip(4);

		assertEquals(c1, c1);
		assertEquals(c1, c2);
		assertFalse(c1.equals(c3));
	}

	@Test
	void testCopyAllele() {

		var c1 = new BitSetChromosome(7);
		var c2 = new BitSetChromosome(7);

		for (int i = 0; i < 7; ++i) {
			c1.alleles().set(i, i % 2 == 0);

			c1.copyAlleleTo(i, c2);
		}

		assertEquals(c1, c2);
	}
}
