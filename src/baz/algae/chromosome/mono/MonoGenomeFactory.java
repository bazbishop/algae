package baz.algae.chromosome.mono;

import baz.algae.IGenome;
import baz.algae.IGenomeFactory;
import baz.algae.chromosome.CrossoverEngine;
import baz.algae.chromosome.IChromosome;
import baz.algae.chromosome.IChromosomeFactory;

public class MonoGenomeFactory implements IGenomeFactory {
	public MonoGenomeFactory( int numParentsPerChild, IChromosomeFactory chromosomeFactory,
			CrossoverEngine reproductionEngine ) {
		mNumParentsPerChild = numParentsPerChild;

		mChromosomeFactory = chromosomeFactory;
		
		mEngine = reproductionEngine;

//		mEngine = new CrossoverEngine( mChromosomeFactory, crossOverChancePerAllele, mutationChancePerAllele );
	}

	@Override
	public IGenome breed( IGenome[] parents ) {
		assert (parents.length == mNumParentsPerChild);

		IChromosome[] parentChromosomes = new IChromosome[ mNumParentsPerChild ];

		for( int p = 0; p < mNumParentsPerChild; ++p ) {
			parentChromosomes[ p ] = ((MonoGenome) parents[ p ]).chromosome();
		}

		IChromosome childChromosome = mEngine.crossOver( parentChromosomes );

		return new MonoGenome( childChromosome );
	}

	@Override
	public IGenome createRandom() {
		return new MonoGenome( mChromosomeFactory.createRandomChromosome() );
	}

	@Override
	public int getNumberOfParents() {
		return mNumParentsPerChild;
	}
	private final IChromosomeFactory mChromosomeFactory;

	private final CrossoverEngine mEngine;

	private final int mNumParentsPerChild;
}
