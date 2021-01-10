package baz.algae.chromosome;

import baz.algae.chromosome.CrossoverEngine;
import baz.algae.chromosome.IChromosome;
import baz.algae.chromosome.IChromosomeFactory;
import junit.framework.TestCase;

public class TestCrossoverEngine extends TestCase
{
	final int MUTATED = 9999;

	final int CHROMOSOME_LEN = 100;

	class Chromosome implements IChromosome
	{
		Chromosome()
		{
			mValues = new int[CHROMOSOME_LEN];
		}

		public void copyAlleleTo( int index, IChromosome target )
		{
			Chromosome rhs = (Chromosome) target;
			rhs.mValues[ index ] = mValues[ index ];
		}

		public int length()
		{
			return mValues.length;
		}

		int[] mValues;
	}

	class Factory implements IChromosomeFactory
	{
		public IChromosome createEmptyChromosome()
		{
			return new Chromosome();
		}

		int aa = 1;

		public IChromosome createRandomChromosome()
		{
			Chromosome ch = new Chromosome();
			for( int a = 0; a < ch.mValues.length; ++a )
			{
				ch.mValues[ a ] = aa++;
			}
			return ch;
		}

		public void mutateAllele( IChromosome chromosome, int index )
		{
			Chromosome ch = (Chromosome) chromosome;

			ch.mValues[ index ] = MUTATED;
		}
	}

	CrossoverEngine mEngine;

	Factory mFactory;

	@Override
    protected void setUp() throws Exception
	{
		mFactory = new Factory();
		mEngine = new CrossoverEngine( mFactory, 0.2, 0 );
	}

	public void testMutation()
	{
		CrossoverEngine engine = new CrossoverEngine( new Factory(), 0.2, 0.3 );

		IChromosome parents[] = new IChromosome[] { mFactory.createRandomChromosome(),
		                mFactory.createRandomChromosome() };

		IChromosome c = engine.crossOver( parents );
		Chromosome ch = (Chromosome) c;

		boolean mutationFound = false;

		for( int a = 0; a < CHROMOSOME_LEN; ++a )
		{
			if( ch.mValues[ a ] == MUTATED )
				mutationFound = true;
		}

		if( !mutationFound )
			fail( "No mutations found in child." );
	}

	public void testCrossOver()
	{
		CrossoverEngine engine = new CrossoverEngine( new Factory(), 0.4, 0 );

		IChromosome parents[] = new IChromosome[] { mFactory.createRandomChromosome(),
		                mFactory.createRandomChromosome(), mFactory.createRandomChromosome() };

		IChromosome c = engine.crossOver( parents );
		Chromosome ch = (Chromosome) c;

		boolean[] pFound = new boolean[parents.length];

		for( int a = 0; a < CHROMOSOME_LEN; ++a )
		{
			int aValue = ch.mValues[ a ];

			boolean matches = false;
			for( int p = 0; p < parents.length; ++p )
			{
				if( ((Chromosome) parents[ p ]).mValues[ a ] == aValue )
				{
					pFound[ p ] = true;
					matches = true;
					break;
				}
			}
			if( !matches )
			{
				fail( "Child allele not present in any parent" );
				break;
			}
		}

		for( int p = 0; p < parents.length; ++p )
		{
			if( !pFound[ p ] )
				fail( "No part of parent chromosome appeared in the child" );
		}
	}

}
