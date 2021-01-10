package baz.algae.chromosome;

import baz.algae.util.Random;

public class CrossoverEngine {
	public CrossoverEngine( IChromosomeFactory factory, double crossOverChancePerAllele, double mutationChancePerAllele ) {
		assert factory != null;
		assert crossOverChancePerAllele >= 0.0;
		assert crossOverChancePerAllele <= 1.0;
		assert mutationChancePerAllele >= 0.0;
		assert mutationChancePerAllele <= 1.0;

		mFactory = factory;
		mCrossOverChancePerAllele = crossOverChancePerAllele;
		mMutationChancePerAllele = mutationChancePerAllele;
	}

	public IChromosome crossOver( IChromosome[] chromosomes ) {
		int c = baz.algae.util.Random.nextInt( chromosomes.length );
		final int len = chromosomes[ 0 ].length();

		final IChromosome result = mFactory.createEmptyChromosome();

		for( int allele = 0; allele < len; ++allele ) {
			chromosomes[ c ].copyAlleleTo( allele, result );

			if( mCrossOverChancePerAllele != 0.0 ) {
				if( Random.nextDouble() < mCrossOverChancePerAllele ) {
					if( chromosomes.length == 2 )
						c = c == 0 ? 1 : 0;
					else
						c = Random.nextNewInt( chromosomes.length, c );
				}
			}

			if( mMutationChancePerAllele != 0.0 ) {
				if( Random.nextDouble() < mMutationChancePerAllele )
					mFactory.mutateAllele( result, allele );
			}
		}

		return result;
	}

	public double getCrossOverChancePerAllele() {
		return mCrossOverChancePerAllele;
	}

	public void setCrossOverChancePerAllele(double crossOverChancePerAllele) {
		mCrossOverChancePerAllele = crossOverChancePerAllele;
	}

	public double getMutationChancePerAllele() {
		return mMutationChancePerAllele;
	}

	public void setMutationChancePerAllele(double mutationChancePerAllele) {
		mMutationChancePerAllele = mutationChancePerAllele;
	}

	private final IChromosomeFactory mFactory;

	private double mMutationChancePerAllele;
	private double mCrossOverChancePerAllele;
}
