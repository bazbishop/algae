package baz.algae.chromosome;

public abstract class GenericChromosomeFactory<E> implements IChromosomeFactory {
	public IChromosome createEmptyChromosome() {
		E[] alleles = makeAllelesArray();

		if( !arrayContainsObjects() ) {
			for( int a = 0; a < alleles.length; ++a )
				alleles[ a ] = emptyAllele();
		}

		return new GenericChromosome<E>( alleles );
	}

	public IChromosome createRandomChromosome() {
		E[] alleles = makeAllelesArray();

		for( int a = 0; a < alleles.length; ++a )
			alleles[ a ] = randomAllele();

		return new GenericChromosome<E>( alleles );
	}

	public void mutateAllele( IChromosome chromosome, int index ) {
		GenericChromosome<E> ch = (GenericChromosome<E>) chromosome;

		ch.mAlleles[ index ] = randomAllele();
	}

	protected abstract E randomAllele();

	protected abstract E emptyAllele();

	protected abstract boolean arrayContainsObjects();

	protected abstract E[] makeAllelesArray();
}
