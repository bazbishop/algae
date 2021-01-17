package algae;

import algae.chromosome.IntegerArrayChromosome;
import algae.fitness.IntegerFitness;
import junit.framework.*;

public class TestMember extends TestCase {
	Genome mGenome;
	IntegerFitness mFitness;

	@Override
	protected void setUp() throws Exception {
		var c1 = new IntegerArrayChromosome(1);
		var c2 = new IntegerArrayChromosome(1);
		c1.alleles()[0] = 1;
		c2.alleles()[0] = 2;
		mGenome = new Genome(new IChromosome[][] { new IChromosome[] { c1, c2 } });

		mFitness = new IntegerFitness(17, false);
	}

	public void testGenome() {
		var member = new Member(mGenome);
		assertEquals(member.genome(), mGenome);

		member = new Member(mGenome, mFitness);
		assertEquals(member.genome(), mGenome);
	}

	public void testCompareTo() {
		var m1 = new Member(mGenome, new IntegerFitness(1, false));
		var m2 = new Member(mGenome, new IntegerFitness(2, false));
		var m3 = new Member(mGenome, new IntegerFitness(3, false));

		assertTrue(m1.compareTo(m2) < 0);
		assertTrue(m2.compareTo(m1) > 0);

		assertTrue(m1.compareTo(m1) == 0);
		assertTrue(m2.compareTo(m2) == 0);
		assertTrue(m3.compareTo(m3) == 0);

		assertTrue(m2.compareTo(m3) < 0);
		assertTrue(m3.compareTo(m2) > 0);
	}
}