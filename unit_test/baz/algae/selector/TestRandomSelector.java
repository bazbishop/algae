package baz.algae.selector;

import java.util.ArrayList;
import java.util.List;

import baz.algae.Member;
import baz.algae.fitness.IntegerFitness;
import baz.algae.selector.RandomSelector;
import junit.framework.TestCase;

public class TestRandomSelector extends TestCase
{
	private RandomSelector mSelector;

	public void testSelect()
	{
		List<Member> members = new ArrayList<Member>();

		final int MEMBER_COUNT = 1000;
		final int SELECT_COUNT = 1000;

		// Use the fitness to store the member index.
		for( int i = 0; i < MEMBER_COUNT; ++i )
		{
			Member m = new Member( null );
			m.fitness = new IntegerFitness( i, false );
			members.add( m );
		}

		int partitionPoint = MEMBER_COUNT / 3;
		int below = 0, above = 0;

		for( int i = 0; i < SELECT_COUNT; ++i )
		{
			int selected = mSelector.select( MEMBER_COUNT );

			if( selected < partitionPoint )
				++below;
			else
				++above;
		}

		assertTrue( below > above );
	}

	@Override
	protected void setUp() throws Exception
	{
		mSelector = new RandomSelector( 2, 0 );
	}
}
