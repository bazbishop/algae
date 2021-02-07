package salesman;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import algae.chromosome.IntegerArrayChromosome;
import algae.util.Rand;

class TestRouteFactoryAndCrossover {

	@Test
	void testCreateRandom() {
		for(int size = 10; size <= 1000000; size *= 10) {
			var f = new RouteFactory(size);

			var chr = f.createRandomChromosome();

			validateRoute(chr.alleles());
		}
	}

	@Test
	void testMutateAllele() {
		var f = new RouteFactory(100);

		var chr = f.createRandomChromosome();

		for(int i = 0; i < chr.alleles().length; ++i) {
			chr.alleles()[i] = i;
		}

		for(int i = 0; i < 100000; ++i) {
			f.mutateAllele(chr,  Rand.nextInt(100));

			validateRoute(chr.alleles());
		}

		verifyNotInOrder(chr.alleles());
	}

	@Test
	void testCrossover() {

		var f = new RouteFactory(100);

		var c1 = f.createRandomChromosome();
		var c2 = f.createRandomChromosome();

		verifyNotInOrder(c1.alleles());
		verifyNotInOrder(c1.alleles());

		var chromosomes = new IntegerArrayChromosome[] {c1, c2};

		var crossover = new Crossover(0.0);
		for(int i = 0; i < 100; ++i ) {
			var child = crossover.apply(chromosomes, f);
			assertTrue(Arrays.equals(c1.alleles(), child.alleles()) || Arrays.equals(c2.alleles(), child.alleles()));
			validateRoute(child.alleles());
		}

		crossover = new Crossover(1.0);

		for(int i = 0; i < 100; ++i ) {
			var child = crossover.apply(chromosomes, f);
			assertFalse(Arrays.equals(c1.alleles(), child.alleles()) || Arrays.equals(c2.alleles(), child.alleles()));
			validateRoute(child.alleles());
		}
	}

	void verifyNotInOrder(int[] cities ) {

		for(int i = 0; i < cities.length; ++i) {
			int city = cities[i];

			if(city != i)
				return;
		}

		fail("City list is in ascending order");
	}

	void validateRoute(int[] cities) {
		var set = new HashSet<Integer>();

		for(int i = 0; i < cities.length; ++i) {
			int city = cities[i];

			assertTrue(city >= 0);
			assertTrue(city < cities.length);

			assertTrue(set.add(city));
		}
	}
}
