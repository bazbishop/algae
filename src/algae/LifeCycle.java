package algae;

import java.util.List;

/**
 * A life-cycle manager.
 */
public class LifeCycle {

	public LifeCycle(IParameters parameters, int multiplicityOfGenome, int numberOfParents, List<IChromosomeFactory> chromosomeFactories, IPhenotypeMapper phenotypeMapper, IFitnessTester fitnessTester) {
		this.parameters = parameters;
		this.multiplicityOfGenome = multiplicityOfGenome;
		this.numberOfParents = numberOfParents;
		
		assert multiplicityOfGenome % numberOfParents == 0;
		
		this.chromosomeFactories = chromosomeFactories;
		this.phenotypeMapper = phenotypeMapper;
		this.fitnessTester = fitnessTester;
	}
	
	
	private final IParameters parameters;
	private final int multiplicityOfGenome;
	private final int numberOfParents;

	private final List<IChromosomeFactory> chromosomeFactories;
	private final IPhenotypeMapper phenotypeMapper;
	private final IFitnessTester fitnessTester;
}
