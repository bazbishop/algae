package algae.operators.mutation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import algae.chromosome.BitSetChromosomeFactory;

class TestMultipleMutation {

	@Test
	void testForMultipleMutations() {

		var never = new MultipleMutation(0.0);
		var sometimes = new MultipleMutation(0.5);
		var always = new MultipleMutation(1.0);

		var factory = new BitSetChromosomeFactory(100);

		for (int i = 0; i < 100; ++i) {
			var c = factory.createEmptyChromosome();
			never.apply(c, factory);
			assertEquals(0, c.alleles().cardinality());

			c = factory.createEmptyChromosome();
			sometimes.apply(c, factory);
			int count = c.alleles().cardinality();
			assertTrue(count > 5 && count < 95);

			c = factory.createEmptyChromosome();
			always.apply(c, factory);
			assertEquals(100, c.alleles().cardinality());
		}
	}
}
