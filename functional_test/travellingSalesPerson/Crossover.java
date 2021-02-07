package travellingSalesPerson;

import java.util.ArrayList;
import java.util.HashSet;

import algae.IChromosome;
import algae.IChromosomeFactory;
import algae.ICrossoverOperator;
import algae.chromosome.IntegerArrayChromosome;
import algae.util.Rand;

class Crossover implements ICrossoverOperator {

	public Crossover(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	@Override
	public IntegerArrayChromosome apply(IChromosome[] input, IChromosomeFactory factory) {

		IntegerArrayChromosome c1;
		IntegerArrayChromosome c2;
		if(Rand.nextBoolean()) {
			c1 = (IntegerArrayChromosome) input[0];
			c2 = (IntegerArrayChromosome) input[1];
		}
		else {
			c1 = (IntegerArrayChromosome) input[1];
			c2 = (IntegerArrayChromosome) input[0];
		}

		if(Rand.test(crossoverProbability)) {

			int[] r1 = c1.alleles();
			int[] r2 = c2.alleles();
			int len = r1.length;

			int copyFromStart = Rand.nextInt(len);
			int copyFromEnd = Rand.nextInt(len);
			if(copyFromStart > copyFromEnd) {
				int temp = copyFromStart;
				copyFromStart = copyFromEnd;
				copyFromEnd = temp;
			}
			else if(copyFromStart == copyFromEnd) {
				return c1.clone();
			}

			int copyLen = copyFromEnd - copyFromStart;

			var set = new HashSet<Integer>();
			for(int a = 0; a < copyLen; ++a) {
				set.add(r2[a + copyFromStart]);
			}

			int insertAt = Rand.nextInt(len);

			var result = new ArrayList<Integer>();

			for(int a = 0; a <insertAt; ++a) {
				if(!set.contains(r1[a]))
					result.add(r1[a]);
			}

			for(int a = copyFromStart; a < copyFromEnd; ++a) {
				result.add(r2[a]);
			}

			for(int a = insertAt; a < len; ++a) {
				if(!set.contains(r1[a]))
					result.add(r1[a]);
			}

			assert result.size() == len;

			var chr = new IntegerArrayChromosome(len);
			for(int a = 0; a < len; ++a) {
				chr.alleles()[a] = result.get(a);
			}
			return chr;
		}
		else {
			return c1.clone();
		}
	}

	private final double crossoverProbability;
}