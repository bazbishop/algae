package travellingSalesPerson;

import algae.IFitness;
import algae.IFitnessTester;
import algae.fitness.DoubleFitness;

class RouteLength implements IFitnessTester {

	public RouteLength(WorldMap world) {
		this.world = world;
	}

	@Override
	public IFitness fitness(Object phenotype) {
		int[] route = (int[]) phenotype;

		double distance = 0.0;

		for(int c = 0; c < route.length; ++c) {
			int start = route[c];
			int end = route[(c+1) % route.length];

			distance += world.distance(start, end);
		}

		return new DoubleFitness(-distance, false);
	}

	private final WorldMap world;
}