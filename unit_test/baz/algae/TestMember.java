package baz.algae;

import baz.algae.IGenome;
import baz.algae.Member;
import baz.algae.fitness.IntegerFitness;
import junit.framework.*;



public class TestMember extends TestCase
{
	static class Genome implements IGenome
	{
	}

	Genome mGenome;

	Member mMember;

	IntegerFitness mFitness;

	@Override
    protected void setUp() throws Exception
	{
		mGenome = new Genome();
		mMember = new Member( mGenome );

		mFitness = new IntegerFitness( 17, false );
		mMember.fitness = mFitness;
	}

	public void testGenome()
	{
		assertEquals( mMember.genome, mGenome );
	}

	public void testCompareTo()
	{
		{
			Member less = new Member( null );
			less.fitness = new IntegerFitness( mFitness.mValue - 1, false );
			assertTrue( less.compareTo( mMember ) < 0 );
		}

		{
			Member more = new Member( null );
			more.fitness = new IntegerFitness( mFitness.mValue + 1, false );
			assertTrue( more.compareTo( mMember ) > 0 );
		}

		{
			Member same = new Member( null );
			same.fitness = new IntegerFitness( mFitness.mValue, false );
			assertTrue( same.compareTo( mMember ) == 0 );
		}
	}
}
