package algae.operators;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.ICrossoverOperator;
import algae.util.Rand;

/**
 * Crossover that can occur multiple times in the same operation.
 */
public class MultipleCrossover implements ICrossoverOperator {

	/**
	 * Constructor.
	 * 
	 * @param crossOverProbabilityPerAllele The probability at the encoding of every
	 *                                      allele that crossover might occur in the
	 *                                      range [0.0, 1.0]
	 */
	public MultipleCrossover(double crossOverProbabilityPerAllele) {
		this.crossOverProbabilityPerAllele = crossOverProbabilityPerAllele;
	}

	@Override
	public IChromosome apply(IChromosome[] input, IChromosomeFactory factory) {

		var result = factory.createEmptyChromosome();
		int len = result.length();
		int count = input.length;

		int c = Rand.nextInt(count);

		for (int allele = 0; allele < len; ++allele) {
			input[c].copyAlleleTo(allele, result);

			if (Rand.test(crossOverProbabilityPerAllele)) {
				if (count == 2)
					c = c == 0 ? 1 : 0;
				else
					c = Rand.nextNewInt(count, c);
			}
		}

		return result;
	}

	private final double crossOverProbabilityPerAllele;
}
