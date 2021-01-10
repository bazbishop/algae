package baz.algae.chromosome;

import java.util.Arrays;

public class IntegerChromosome implements IChromosome {
	public IntegerChromosome( int length ) {
		mAlleles = new int[ length ];
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		for( int i = 0; i < mAlleles.length; ++i ) {
			if( i > 0 )
				result.append( ':' );
			result.append( mAlleles[ i ] );
		}
		return result.toString();
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;

		IntegerChromosome rhs = (IntegerChromosome) o;
		return Arrays.equals( mAlleles, rhs.mAlleles );
	}

	@Override
	public int hashCode() {
		return mAlleles.hashCode();
	}

	public void copyAlleleTo( int index, IChromosome target ) {
		IntegerChromosome rhs = (IntegerChromosome) target;

		rhs.mAlleles[ index ] = mAlleles[ index ];
	}

	public int length() {
		return mAlleles.length;
	}

	public final int[] mAlleles;
}
