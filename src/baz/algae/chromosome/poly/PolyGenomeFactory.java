package baz.algae.chromosome.poly;

import baz.algae.IGenome;
import baz.algae.IGenomeFactory;
import baz.algae.chromosome.CrossoverEngine;
import baz.algae.chromosome.IChromosome;
import baz.algae.chromosome.IChromosomeFactory;

public class PolyGenomeFactory implements IGenomeFactory {
	public PolyGenomeFactory( int numParentsPerChild, int numChromosomesPerIndividual,
	                IChromosomeFactory chromosomeFactory,
	                CrossoverEngine reproductionEngine ) {
		assert (numChromosomesPerIndividual % numParentsPerChild == 0);

		mNumParentsPerChild = numParentsPerChild;
		mNumChromosomesPerIndividual = numChromosomesPerIndividual;
		mNumChromosomesPerGamete = mNumChromosomesPerIndividual / mNumParentsPerChild;

		mChromosomeFactory = chromosomeFactory;

		mEngine = reproductionEngine;
	}

	@Override
	public IGenome breed( IGenome[] parents ) {
		assert (parents.length == mNumParentsPerChild);

		// Make gametes
		PolyGenome[] gametes = new PolyGenome[ mNumParentsPerChild ];

		for( int g = 0; g < mNumParentsPerChild; ++g )
			gametes[ g ] = makeGamete( (PolyGenome) parents[ g ] );

		return combineGametes( gametes );
	}

	protected PolyGenome makeGamete( PolyGenome parent ) {
		IChromosome[] childChromosomes = new IChromosome[ mNumChromosomesPerGamete ];

		// ...
		// Group chromosomes in to mNumChromosomesPerGamete groups of
		// mNumParentsPerChild chromosomes
		for( int g = 0; g < mNumChromosomesPerGamete; ++g ) {
			IChromosome[] forCrossOver = new IChromosome[ mNumParentsPerChild ];

			for( int f = 0; f < mNumParentsPerChild; ++f ) {
				forCrossOver[ f ] = parent.chomosomes()[ g * mNumParentsPerChild + f ];
			}

			childChromosomes[ g ] = mEngine.crossOver( forCrossOver );
		}

		// For each group do xover + mutation to make one new child chromosome
		// and add to childChromosomes.

		return new PolyGenome( childChromosomes );
	}

	protected PolyGenome combineGametes( PolyGenome[] gametes ) {
		IChromosome[] childChromosomes = new IChromosome[ mNumChromosomesPerIndividual ];

		int child = 0;
		for( int c = 0; c < gametes[ 0 ].chomosomes().length; ++c ) {
			for( int p = 0; p < mNumParentsPerChild; ++p ) {
				childChromosomes[ child ] = gametes[ p ].chomosomes()[ c ];
				++child;
			}
		}

		return new PolyGenome( childChromosomes );
	}

	@Override
	public IGenome createRandom() {
		IChromosome[] chromosomes = new IChromosome[ mNumChromosomesPerIndividual ];

		for( int c = 0; c < chromosomes.length; ++c )
			chromosomes[ c ] = mChromosomeFactory.createRandomChromosome();

		return new PolyGenome( chromosomes );
	}

	@Override
	public int getNumberOfParents() {
		return mNumParentsPerChild;
	}
	private final IChromosomeFactory mChromosomeFactory;

	private final CrossoverEngine mEngine;

	private final int mNumParentsPerChild;

	private final int mNumChromosomesPerIndividual;

	private final int mNumChromosomesPerGamete;
}
