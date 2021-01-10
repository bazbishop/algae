package baz.algae;

public interface IGenomeFactory {
	IGenome createRandom();

	// TODO Possible break this out to IGenomeFactory.makeGametes() => ReproductionEngine.crossover() => IGenomeFactory.combineGametes()
	IGenome breed( IGenome[] parents );
	
	int getNumberOfParents();
}
