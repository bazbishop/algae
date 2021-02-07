package algae;

import algae.chromosome.BitSetChromosome;
import algae.chromosome.IntegerArrayChromosome;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestGenome {

	@Test
	void testValidChromosomes() {
		var chromosomes = makeChromosomes(4, 7);

		var genome = new Genome(chromosomes);

		assertEquals(7, genome.chromosomes().length);
		assertEquals(4, genome.chromosomes()[0].length);
	}

	private IChromosome[][] makeChromosomes(int multiplicity, int numGroups) {
		var chromosomes = new IChromosome[numGroups][];

		for (int g = 0; g < numGroups; ++g) {
			var chromosomeSet = new IChromosome[multiplicity];
			for (int i = 0; i < multiplicity; ++i) {

				int len = g + 1;
				if (g % 2 == 0) {
					var c = new IntegerArrayChromosome(len);

					for (int j = 0; j < len; ++j)
						c.alleles()[j] = j + 1;

					chromosomeSet[i] = c;
				}
				else {
					var c = new BitSetChromosome(len);

					for (int j = 0; j < len; ++j)
						c.alleles().set(j, j % 2 == 0);

					chromosomeSet[i] = c;
				}
			}
			chromosomes[g] = chromosomeSet;
		}

		return chromosomes;
	}

	@Test
	void testInvalidChromosomes() {

		// One group with wrong multiplicity
		var chromosomes = makeChromosomes(4, 7);

		var g2 = chromosomes[2];
		g2 = new IChromosome[] { g2[0], g2[1], g2[2] };
		chromosomes[2] = g2;

		checkInvalid(chromosomes);

		// One group with wrong differently typed chromosomes
		chromosomes = makeChromosomes(4, 7);
		g2 = chromosomes[2];
		var g3 = chromosomes[3];

		// Swap two chromosomes from different groups
		var temp = g2[2];
		g2[2] = g3[2];
		g3[2] = temp;

		checkInvalid(chromosomes);

		// One chromosome in a group with wrong length
		chromosomes = makeChromosomes(4, 7);
		g2 = chromosomes[2];

		int len = g2[3].length();
		g2[3] = new IntegerArrayChromosome(len + 1);

		checkInvalid(chromosomes);
	}

	private void checkInvalid(IChromosome[][] chromosomes) {

		try {
			new Genome(chromosomes);
			fail("Genome constructor should throw an exeption if the chromosome structure is invalid");
		} catch (AssertionError e) {
		}
	}

	@Test
	void testEquality() {

		var chromosomes = makeChromosomes(4, 7);
		var genome1 = new Genome(chromosomes);

		chromosomes = makeChromosomes(4, 7);
		var genome2 = new Genome(chromosomes);

		assertEquals(genome1, genome2);

		var g2 = chromosomes[2];

		var chr = (IntegerArrayChromosome) g2[3];
		chr.alleles()[1]++;

		assertFalse(genome1.equals(genome2));
	}

	@Test
	void testCombine() {

		var chromosomes = makeChromosomes(4, 7);
		var genome1 = new Genome(chromosomes);

		chromosomes = makeChromosomes(4, 7);
		var genome2 = new Genome(chromosomes);

		var combined = genome1.combine(genome2);

		var result = combined.chromosomes();

		assertEquals(7, result.length);

		for (int g = 0; g < result.length; ++g) {
			var group = result[g];

			assertEquals(8, group.length);

			for (int c = 0; c < group.length; ++c) {

				var chromosome = group[c];

				assertEquals(g + 1, chromosome.length());

				if (g % 2 == 0)
					assertTrue(chromosome instanceof IntegerArrayChromosome);
				else
					assertTrue(chromosome instanceof BitSetChromosome);
			}
		}
	}
}
