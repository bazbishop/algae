package baz.algae;

import java.util.List;

import junit.framework.TestCase;
import baz.algae.fitness.IntegerFitness;
import baz.algae.selector.RandomSelector;
import baz.algae.util.Random;

public class TestStdLifeCycle extends TestCase {
	final int mPopulationSize = 100;

	int mMaxGenerations = 200;
	
	IPhenotypeMapper mMapper;

	IFitnessTester mTester;

	IGenomeFactory mFactory;

	ISelector mSelector;

	SubjectStdLifeCycle mLifeCycle;
	
	class PhenotypeMapper implements IPhenotypeMapper {
		public Integer createPhenotype(IGenome genome) {
			Genome g = (Genome) genome;
			return g.mValue;
		}
	}

	class FitnessTester implements IFitnessTester {
		public IFitness fitness(Object phenotype) {
			return new IntegerFitness( (Integer) phenotype, false);
		}
	}

	class Genome implements IGenome {
		public Genome(int value) {
			mValue = value;
		}

		int mValue;
	}

	final int MIN_GENOME_VALUE = 1;

	final int MAX_GENOME_VALUE = 50;

	class GenomeFactory implements IGenomeFactory {
		public IGenome breed(IGenome[] parents) {
			int total = 0;
			for (IGenome ig : parents)
				total += ((Genome) ig).mValue;

			return new Genome(total / parents.length);
		}

		@Override
		public IGenome createRandom() {
			return new Genome(Random.nextInt(MAX_GENOME_VALUE
					- MIN_GENOME_VALUE)
					+ MIN_GENOME_VALUE);
		}

		@Override
		public int getNumberOfParents() {
			return 2;
		}
	}

	static class SubjectStdLifeCycle extends StdLifeCycle {
		public SubjectStdLifeCycle(int populationSize, int maxGenerations,
				IPhenotypeMapper mapper,
				IFitnessTester tester, IGenomeFactory factory,
				ISelector selector) {
			super(populationSize, mapper, tester, factory, selector);
		}

		@Override
		public void breedNextGeneration() {
			super.breedNextGeneration();
		}

		@Override
		public void createRandomPopulation() {
			super.createRandomPopulation();
		}

		@Override
		public boolean initGeneration() {
			return super.initGeneration();
		}

		@Override
		public void measureFitness() {
			super.measureFitness();
		}

		@Override
		public void sort() {
			super.sort();
		}

		public List<Member> getCurrentPopulation() {
			return mCurrentPopulation;
		}

		public boolean isFinished() {
			return mFinished;
		}
	}

	@Override
	protected void setUp() throws Exception {
		
		mMapper = new PhenotypeMapper();
		mTester = new FitnessTester();
		mFactory = new GenomeFactory();
		mSelector = new RandomSelector();

		mLifeCycle = new SubjectStdLifeCycle(mPopulationSize, mMaxGenerations,
				mMapper, mTester, mFactory, mSelector);
	}

	public void testCreateRandomPopulation() {
		mLifeCycle.createRandomPopulation();

		List<Member> pop = mLifeCycle.getCurrentPopulation();

		assertEquals(pop.size(), mPopulationSize);

		for (Member m : pop) {
			assertTrue(m.fitness == null);
			Genome g = (Genome) m.genome;
			assertTrue(g.mValue >= MIN_GENOME_VALUE);
			assertTrue(g.mValue <= MAX_GENOME_VALUE);
		}
	}

	public void testMeasureFitness() {
		mLifeCycle.createRandomPopulation();
		mLifeCycle.measureFitness();

		List<Member> pop = mLifeCycle.getCurrentPopulation();

		for (Member m : pop) {
			assertTrue(m.fitness != null);
			IntegerFitness f = (IntegerFitness) m.fitness;

			Genome g = (Genome) m.genome;

			assertEquals(f.mValue, g.mValue);
		}
	}

	public void testBreedNextGeneration() {
		mLifeCycle.createRandomPopulation();
		mLifeCycle.measureFitness();
		mLifeCycle.breedNextGeneration();

		List<Member> pop = mLifeCycle.getCurrentPopulation();

		assertEquals(pop.size(), mPopulationSize);

		for (Member m : pop) {
			assertTrue(m.fitness == null);
			Genome g = (Genome) m.genome;
			assertTrue(g.mValue >= MIN_GENOME_VALUE);
			assertTrue(g.mValue <= MAX_GENOME_VALUE);
		}
	}

	public void testGetParentGenomes() {
		Member[] members = new Member[3];
		Genome[] genomes = new Genome[members.length];

		for (int m = 0; m < members.length; ++m) {
			genomes[m] = new Genome(m);
			members[m] = new Member(genomes[m]);
		}

		IGenome[] result = StdLifeCycle.getParentGenomes(members);
		for (int m = 0; m < members.length; ++m) {
			assertTrue(genomes[m] == result[m]);
		}
	}

	public void testSort() {
		mLifeCycle.createRandomPopulation();
		mLifeCycle.measureFitness();
		mLifeCycle.sort();

		List<Member> pop = mLifeCycle.getCurrentPopulation();

		assertEquals(pop.size(), mPopulationSize);

		int last = 0;
		for (Member m : pop) {
			IntegerFitness f = (IntegerFitness) m.fitness;
			assertTrue(f.mValue >= last);
			last = f.mValue;
		}
	}

	public void testInitGeneration() {
		// fail( "Not yet implemented" );
	}

	public void testRunGeneration() {
		// fail( "Not yet implemented" );
	}
}
