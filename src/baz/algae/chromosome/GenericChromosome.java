package baz.algae.chromosome;

import java.util.Arrays;

public class GenericChromosome<E> implements IChromosome {
	public GenericChromosome( E[] alleles ) {
		mAlleles = alleles;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();

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

		GenericChromosome<E> rhs = (GenericChromosome<E>) o;
		return Arrays.equals( mAlleles, rhs.mAlleles );
	}

	@Override
	public int hashCode() {
		return mAlleles.hashCode();
	}

	public void copyAlleleTo( int index, IChromosome target ) {
		GenericChromosome<E> rhs = (GenericChromosome<E>) target;

		rhs.mAlleles[ index ] = mAlleles[ index ];
	}

	public int length() {
		return mAlleles.length;
	}

	public final E[] mAlleles;
}
