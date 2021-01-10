package baz.algae.chromosome.poly;

import java.math.BigInteger;

import baz.algae.IFitness;
import baz.algae.IFitnessTester;
import baz.algae.IGenome;
import baz.algae.IPhenotypeMapper;
import baz.algae.chromosome.CrossoverEngine;
import baz.algae.chromosome.IChromosome;
import baz.algae.chromosome.IChromosomeFactory;
import baz.algae.chromosome.poly.PolyGenome;
import baz.algae.chromosome.poly.PolyGenomeFactory;
import baz.algae.fitness.IntegerFitness;
import baz.algae.util.Random;


import junit.framework.TestCase;

public class TestPolyGenomeFactory extends TestCase
{
	static final int BIT_LENGTH = 32;

	static class Chromosome implements IChromosome
	{
		public Chromosome( BigInteger number ) {
			mNumber = number;
		}

		public void copyAlleleTo( int index, IChromosome target ) {
			Chromosome t = (Chromosome) target;

			if( mNumber.testBit( index ) )
				t.mNumber = t.mNumber.setBit( index );
			else
				t.mNumber = t.mNumber.clearBit( index );
		}

		public int length() {
			return mNumber.bitLength();
		}

		BigInteger mNumber;
	}

	static class ChromosomeFactory implements IChromosomeFactory
	{
		public IChromosome createEmptyChromosome()
		{
			return new Chromosome( BigInteger.ZERO );
		}

		public IChromosome createRandomChromosome()
		{
			BigInteger n = BigInteger.ZERO;

			for( int i = 0; i < BIT_LENGTH; ++i )
			{
				if( Random.nextBoolean() )
					n = n.setBit( i );
			}

			return new Chromosome( n );
		}

		public void mutateAllele( IChromosome chromosome, int index )
		{
			Chromosome ch = (Chromosome) chromosome;
			ch.mNumber = ch.mNumber.flipBit( index );
		}

	}

	static class PhenotypeMapper implements IPhenotypeMapper {
		public Object createPhenotype(IGenome genome) {
			PolyGenome p = (PolyGenome) genome;
			Chromosome c = (Chromosome) p.chomosomes()[ 0 ];
			return c.mNumber;
		}
	}
	
	static class FitnessTester implements IFitnessTester {
		public IFitness fitness( Object phenotype ) {
			BigInteger bigInt = (BigInteger) phenotype;

			int n = 0;

			for( int i = 0; i < BIT_LENGTH; ++i ) {
				if( !bigInt.testBit( i ) )
					--n;
			}

			return new IntegerFitness( n, false );
		}

	}

	class PublicPolyGenomeFactory extends PolyGenomeFactory
	{
		public PublicPolyGenomeFactory( int numParentsPerChild, int numChromosomesPerIndividual,
		                IChromosomeFactory chromosomeFactory, double crossOverChancePerAllele, double mutationChancePerAllele )
		{
			super( numParentsPerChild, numChromosomesPerIndividual, chromosomeFactory,
					new CrossoverEngine(chromosomeFactory, crossOverChancePerAllele, mutationChancePerAllele) );
		}

		@Override
		protected PolyGenome combineGametes( PolyGenome[] gametes )
		{
			return super.combineGametes( gametes );
		}

		@Override
		protected PolyGenome makeGamete( PolyGenome parent )
		{
			return super.makeGamete( parent );
		}

	}

	IChromosomeFactory mChromosomeFactory;

	PublicPolyGenomeFactory mFactory;

	final int numParentsPerChild = 2;

	final int numChromosomesPerIndividual = 6;
	final int chromosomesPerGamete = numChromosomesPerIndividual / numParentsPerChild;

	final double crossOverChancePerAllele = 0.04;

	final double mutationChancePerAllele = 0.01;

	@Override
	protected void setUp() throws Exception
	{
		mChromosomeFactory = new ChromosomeFactory();
		mFactory = new PublicPolyGenomeFactory( numParentsPerChild, numChromosomesPerIndividual, mChromosomeFactory,
		                crossOverChancePerAllele, mutationChancePerAllele );
	}
	
	public void testMakeGamete()
	{
		PolyGenome parent = (PolyGenome) mFactory.createRandom();
		
		PolyGenome gamete = mFactory.makeGamete( parent );
		
		assertEquals( gamete.chomosomes().length, chromosomesPerGamete );

		for( IChromosome c : gamete.chomosomes() )
		{
			assertTrue( c != null );
			assertTrue( c instanceof Chromosome );
		}
	}

	public void testCombineGametes()
	{
		PolyGenome[] gametes = new PolyGenome[numParentsPerChild];

		for( int g = 0; g < numParentsPerChild; ++g )
		{
			IChromosome[] chromosomes = new IChromosome[chromosomesPerGamete];

			for( int c = 0; c < chromosomesPerGamete; ++c )
				chromosomes[ c ] = mChromosomeFactory.createRandomChromosome();

			gametes[ g ] = new PolyGenome( chromosomes );
		}

		PolyGenome pg = mFactory.combineGametes( gametes );

		IChromosome[] childChromosomes = pg.chomosomes();
		
		assertEquals( childChromosomes.length, numChromosomesPerIndividual );
		for( int c1 = 0; c1 < numChromosomesPerIndividual; ++c1 )
		{
			assertTrue( childChromosomes[ c1 ] != null );
			
			for( int c2 = c1 + 1; c2 < numChromosomesPerIndividual; ++c2 )
			{
				assertTrue( childChromosomes[ c1 ] != childChromosomes[ c2 ] );
			}
		}
	}

	public void testCreateRandom()
	{
		PolyGenome pg = (PolyGenome) mFactory.createRandom();

		assertEquals( pg.chomosomes().length, numChromosomesPerIndividual );

		for( IChromosome c : pg.chomosomes() )
		{
			assertTrue( c != null );
			assertTrue( c instanceof Chromosome );
		}
	}
}
