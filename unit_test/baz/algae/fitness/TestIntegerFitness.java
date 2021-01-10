package baz.algae.fitness;

import baz.algae.fitness.IntegerFitness;
import junit.framework.TestCase;

public class TestIntegerFitness extends TestCase
{
	final int VALUE = 17;

	final boolean FINISHED = false;

	IntegerFitness mFitness;

	@Override
	protected void setUp() throws Exception
	{
		mFitness = new IntegerFitness( VALUE, FINISHED );
	}

	public void testIsFinished()
	{
		IntegerFitness notFinished = new IntegerFitness( 0, false );
		assertEquals( notFinished.isFinished(), false );

		IntegerFitness finished = new IntegerFitness( 0, true );
		assertEquals( finished.isFinished(), true );
	}

	public void testCompareTo()
	{
		IntegerFitness less = new IntegerFitness( VALUE - 1, false );
		assertTrue( less.compareTo( mFitness ) < 0 );

		IntegerFitness more = new IntegerFitness( VALUE + 1, false );
		assertTrue( more.compareTo( mFitness ) > 0 );

		IntegerFitness same = new IntegerFitness( VALUE, false );
		assertTrue( same.compareTo( mFitness ) == 0 );
	}

	public void testAttributes()
	{
		IntegerFitness a = new IntegerFitness( VALUE, false );
		assertEquals( a.mValue, VALUE );
		assertEquals( a.mFinished, false );

		IntegerFitness b = new IntegerFitness( VALUE + 1, true );
		assertEquals( b.mValue, VALUE + 1 );
		assertEquals( b.mFinished, true );
	}
}
