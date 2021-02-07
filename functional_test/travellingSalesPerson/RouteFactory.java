package travellingSalesPerson;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.chromosome.IntegerArrayChromosome;
import algae.util.Rand;

class RouteFactory implements IChromosomeFactory {

	public RouteFactory(int numberOfCities) {
		this.numberOfCities = numberOfCities;
	}

	/**
	 * Swap the indicated allele with a second randomly chosen allele.
	 */
	@Override
	public void mutateAllele(IChromosome chromosome, int index) {

		var alleles = (int[]) chromosome.alleles();

		int index2 = Rand.nextInt(numberOfCities);
		int temp = alleles[index];
		alleles[index] = alleles[index2];
		alleles[index2] = temp;
	}

	@Override
	public IntegerArrayChromosome createRandomChromosome() {
		var result = new IntegerArrayChromosome(numberOfCities);

		var alleles = result.alleles();

		for(int c = 0; c < alleles.length; ++c)
			alleles[c] = c;

		Rand.shuffle(alleles);

		return result;
	}

	@Override
	public IntegerArrayChromosome createEmptyChromosome() {
		// Not used
		return null;
	}

	private final int numberOfCities;
}