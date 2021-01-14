package algae;

/**
 * A set of chromosomes organised into homologous groups.
 * The genome might represent a complete organism or a gamete.
 */
class Genome {

	/**
	 * Constructor
	 * TODO Pass in the chromosomes here?
	 * @param groupCount The number of homologous chromosome groups.
	 * @param multiplicity The multiplicity of the homologous chromosome groups.
	 */
	public Genome(int groupCount, int multiplicity)
	{
		assert groupCount > 0;
		assert multiplicity > 0;
		
		chromosomes = new IChromosome[groupCount][];
		
		for(int g = 0; g < groupCount; ++g)
		{
			chromosomes[g] = new IChromosome[multiplicity];
		}
	}

	public Genome(IChromosome[][] chromosomes)
	{
		assert chromosomes.length > 0;
		// TODO more checking of the group sizes
		
		this.chromosomes = chromosomes;
	}
	
	public IChromosome[][] chromosomes()
	{
		return chromosomes;
	}
	
	public int groupCount()
	{
		return chromosomes.length;
	}
	
	public int multiplicity()
	{
		return chromosomes[0].length;
	}
	
	public IChromosome getChromosome(int groupIndex, int index)
	{
		return chromosomes[groupIndex][index];
	}

	public void setChromosome(int groupIndex, int index, IChromosome chromosome)
	{
		chromosomes[groupIndex][index] = chromosome;
	}
	
	public Genome combine(Genome other)
	{
		assert chromosomes.length == other.chromosomes.length;
		assert chromosomes[0].length == other.chromosomes[0].length;
		
		int groupCount = chromosomes.length;
		int multiplicityThis = chromosomes[0].length;
		int multiplicityCombined = multiplicityThis * 2;
		
		Genome result = new Genome(groupCount, multiplicityCombined);
		
		for(int g = 0; g < groupCount; ++g)
		{
			for(int i = 0; i < multiplicityThis; ++i)
			{
				result.chromosomes[g][i] = chromosomes[g][i];
				result.chromosomes[g][i + multiplicityThis] = other.chromosomes[g][i];
			}
		}
		
		return result;
	}
	
	IChromosome[][] chromosomes;
}
