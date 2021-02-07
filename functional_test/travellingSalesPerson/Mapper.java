package travellingSalesPerson;

import algae.Genome;
import algae.IPhenotypeMapper;
import algae.chromosome.IntegerArrayChromosome;

public class Mapper implements IPhenotypeMapper {

	@Override
	public Object createPhenotype(Genome genome) {
		var chromosome = (IntegerArrayChromosome)genome.chromosomes()[0][0];
		return chromosome.alleles();
	}
}