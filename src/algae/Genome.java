package algae;

import java.util.Arrays;

/**
 * A set of chromosomes organised into homologous groups. The genome might
 * represent a complete organism or a gamete.
 */
public class Genome {

	/**
	 * Constructor.
	 * 
	 * @param chromosomes The chromosomes organised into homologous groups.
	 */
	public Genome(IChromosome[][] chromosomes) {
		this.chromosomes = chromosomes;

		assert validateChromosomes();
	}

	private boolean validateChromosomes() {
		if (chromosomes == null)
			return false;

		if (chromosomes.length <= 0)
			return false;

		int len = chromosomes[0].length;
		for (int c = 1; c < chromosomes.length; ++c) {
			if (len != chromosomes[c].length)
				return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return 31 + Arrays.deepHashCode(chromosomes);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;

		if (obj == null)
			return false;
		
		if(!(obj instanceof Genome))
			return false;
		
		Genome other = (Genome) obj;
		
		if (!Arrays.deepEquals(chromosomes, other.chromosomes))
			return false;
		
		return true;
	}

	/**
	 * Get the chromosomes.
	 * 
	 * @return A 2D array of chromosomes organised by homologous groups and
	 *         multiplicity.
	 */
	public IChromosome[][] chromosomes() {
		return chromosomes;
	}

	/**
	 * Combine the chromosomes of two genomes, during
	 * 
	 * @param other The 'other' genome to combine with this one.
	 * @return A new, merged genome.
	 */
	public Genome combine(Genome other) {
		assert chromosomes.length == other.chromosomes.length;
		assert chromosomes[0].length == other.chromosomes[0].length;

		int groupCount = chromosomes.length;
		int multiplicityThis = chromosomes[0].length;
		int multiplicityCombined = multiplicityThis * 2;

		var result = new IChromosome[groupCount][multiplicityCombined];

		for (int g = 0; g < groupCount; ++g) {
			for (int i = 0; i < multiplicityThis; ++i) {
				result[g][i] = chromosomes[g][i];
				result[g][i + multiplicityThis] = other.chromosomes[g][i];
			}
		}

		return new Genome(result);
	}

	private final IChromosome[][] chromosomes;
}
