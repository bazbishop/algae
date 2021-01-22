package algae.operators.crossover;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algae.chromosome.BitSetChromosome;
import algae.chromosome.BitSetChromosomeFactory;

class TestMultipleCrossover {

	private BitSetChromosomeFactory factory;
	private BitSetChromosome c0;
	private BitSetChromosome c1;
	private BitSetChromosome[] input;

	@BeforeEach
	void setup() {
		factory = new BitSetChromosomeFactory(10);
		c0 = factory.createEmptyChromosome();
		c1 = factory.createEmptyChromosome();
		c1.alleles().set(0, 10);

		input = new BitSetChromosome[] { c0, c1 };
	}

	@Test
	void testCrossover() {

		// Crossover at every allele
		var cr = new MultipleCrossover(1.0);

		for (int i = 0; i < 1000; ++i) {
			var child = (BitSetChromosome) cr.apply(input, factory);

			var bits = child.alleles();
			int set = bits.cardinality();
			assertEquals(5, set);

			boolean v = bits.get(0);

			for (int bit = 1; bit < 10; ++bit) {
				boolean newV = bits.get(bit);

				assertNotEquals(v, newV);
				v = newV;
			}
		}
	}

	@Test
	void testCrossoverOneParent() {

		var cr = new MultipleCrossover(1.0);

		input = new BitSetChromosome[] { c0 };
		var child = (BitSetChromosome) cr.apply(input, factory);
		assertEquals(0, child.alleles().cardinality());

		input = new BitSetChromosome[] { c1 };
		child = (BitSetChromosome) cr.apply(input, factory);
		assertEquals(10, child.alleles().cardinality());
	}
}
