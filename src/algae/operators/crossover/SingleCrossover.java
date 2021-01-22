package algae.operators.crossover;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.ICrossoverOperator;
import algae.util.Rand;

/**
 * Crossover that can only occur at a single allele in one operation.
 */
public class SingleCrossover implements ICrossoverOperator {

	/**
	 * Constructor.
	 * 
	 * @param crossOverProbability The probability of a crossover occurring during the operation in the range [0.0, 1.0]
	 */
	public SingleCrossover(double crossOverProbability) {
		this.crossOverProbability = crossOverProbability;
	}

	@Override
	public IChromosome apply(IChromosome[] input, IChromosomeFactory factory) {

		var result = factory.createEmptyChromosome();
		int len = result.length();
		int count = input.length;

		int copyFrom1 = count == 1 ? 0 : Rand.nextInt(count);
		int copyFrom2 = copyFrom1;

		int switchAt = -1;
		if (count > 1 && len > 1 && Rand.test(crossOverProbability)) {

			switchAt = len == 2 ? 1 : Rand.nextInt(len - 1) + 1;

			if (count == 2)
				copyFrom2 = copyFrom1 == 0 ? 1 : 0;
			else
				copyFrom2 = Rand.nextNewInt(count, copyFrom1);
		}

		int copyFrom = copyFrom1;
		for (int allele = 0; allele < len; ++allele) {

			if (allele == switchAt)
				copyFrom = copyFrom2;

			input[copyFrom].copyAlleleTo(allele, result);
		}

		return result;
	}

	private final double crossOverProbability;
}
