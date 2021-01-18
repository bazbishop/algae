package algae.population;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import algae.Genome;
import algae.IChromosome;
import algae.chromosome.IntegerArrayChromosome;
import algae.fitness.IntegerFitness;

class TestMember {
	private Genome mGenome;
	private IntegerFitness mFitness;

	@BeforeEach
	void setUp() throws Exception {
		var c1 = new IntegerArrayChromosome(1);
		var c2 = new IntegerArrayChromosome(1);
		c1.alleles()[0] = 1;
		c2.alleles()[0] = 2;
		mGenome = new Genome(new IChromosome[][] { new IChromosome[] { c1, c2 } });

		mFitness = new IntegerFitness(17, false);
	}

	@Test
	void testGenome() {
		var member = new Member(mGenome);
		assertEquals(member.genome(), mGenome);

		member.setFitness(mFitness);
		assertEquals(member.genome(), mGenome);
	}

	@Test
	void testCompareTo() {
		var m1 = new Member(mGenome);
		var m2 = new Member(mGenome);
		var m3 = new Member(mGenome);

		m1.setFitness(new IntegerFitness(1, false));
		m2.setFitness(new IntegerFitness(2, false));
		m3.setFitness(new IntegerFitness(3, false));

		assertTrue(m1.compareTo(m2) < 0);
		assertTrue(m2.compareTo(m1) > 0);

		assertTrue(m1.compareTo(m1) == 0);
		assertTrue(m2.compareTo(m2) == 0);
		assertTrue(m3.compareTo(m3) == 0);

		assertTrue(m2.compareTo(m3) < 0);
		assertTrue(m3.compareTo(m2) > 0);
	}
}
