package algae.operators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import algae.chromosome.BitSetChromosome;
import algae.chromosome.BitSetChromosomeFactory;

class TestSingleMutation {

	@Test
	void testForSingleMutation() {

		var never = new SingleMutation(0.0);
		var sometimes = new SingleMutation(0.5);
		var always = new SingleMutation(1.0);

		var factory = new BitSetChromosomeFactory(100);

		int sumMaybe = 0;

		for (int i = 0; i < 100; ++i) {
			var c = (BitSetChromosome) factory.createEmptyChromosome();
			never.apply(c, factory);
			assertEquals(0, c.alleles().cardinality());

			c = (BitSetChromosome) factory.createEmptyChromosome();
			sometimes.apply(c, factory);
			int count = c.alleles().cardinality();
			assertTrue(count == 0 || count == 1);
			sumMaybe += c.alleles().cardinality();

			c = (BitSetChromosome) factory.createEmptyChromosome();
			always.apply(c, factory);
			assertEquals(1, c.alleles().cardinality());
		}

		assertTrue(sumMaybe > 5 && sumMaybe < 95);
	}

}
