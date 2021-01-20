package algae.population;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algae.Genome;
import algae.IChromosome;
import algae.chromosome.IntegerArrayChromosome;
import algae.fitness.IntegerFitness;

class TestSimplePopulation {

	private Genome g1;
	private Genome g2;
	private Genome g3;

	private IntegerFitness f1;
	private IntegerFitness f2;

	@BeforeEach
	void setup() {

		var c1 = new IntegerArrayChromosome(1);
		c1.alleles()[0] = 1;

		var c2 = new IntegerArrayChromosome(1);
		c1.alleles()[0] = 2;

		var c3 = new IntegerArrayChromosome(1);
		c1.alleles()[0] = 2;

		g1 = new Genome(new IChromosome[][] { new IChromosome[] { c1 } });
		g2 = new Genome(new IChromosome[][] { new IChromosome[] { c2 } });
		g3 = new Genome(new IChromosome[][] { new IChromosome[] { c3 } });

		f1 = new IntegerFitness(1, false);
		f2 = new IntegerFitness(2, false);
	}

	@Test
	void testAdd() {
		var population = new SimplePopulation(10);
		assertEquals(0, population.size());
		assertEquals(0, population.discarded());

		population.addMember(g1);
		assertEquals(1, population.size());
		assertEquals(0, population.discarded());

		population.addMember(g2);
		assertEquals(2, population.size());
		assertEquals(0, population.discarded());

		population.addMember(g3);
		assertEquals(3, population.size());
		assertEquals(0, population.discarded());

		population.addMember(g1);
		assertEquals(4, population.size());
		assertEquals(0, population.discarded());
	}

	@Test
	void testFitness() {
		var population = new SimplePopulation(10);

		population.addMember(g1);
		population.addMember(g2);
		population.addMember(g3);
		population.addMember(g3);

		population.setFitness(0, f1);
		population.setFitness(1, f2);
		population.setFitness(2, f1);
		population.setFitness(3, f2);

		assertEquals(f1, population.getFitness(0));
		assertEquals(f2, population.getFitness(1));
		assertEquals(f1, population.getFitness(2));
		assertEquals(f2, population.getFitness(3));

		population.sort();

		assertEquals(f2, population.getFitness(0));
		assertEquals(f2, population.getFitness(1));
		assertEquals(f1, population.getFitness(2));
		assertEquals(f1, population.getFitness(3));
	}
}
