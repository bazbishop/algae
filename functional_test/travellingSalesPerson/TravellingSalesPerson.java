package travellingSalesPerson;

import java.util.ArrayList;
import java.util.HashSet;

import algae.CrossoverStrategy;
import algae.LifeCycle;
import algae.Parameters;
import algae.operators.mutation.MultipleMutation;
import algae.population.SimplePopulationFactory;

public class TravellingSalesPerson {

	static final int NUM_CITIES = 50;
	final static int MAX_GENERATIONS = 10000;

	public static void main(String[] args) {

		var world = new WorldMap(NUM_CITIES);
		//var x = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 7, 6, 5, 4, 3, 2, 1};
		//var y = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		//var world = new WorldMap(x, y);

		simpleCheck(world);

		var factory = new RouteFactory(world.count());
		var mapper = new Mapper();
		var tester = new RouteLength(world);

		var parameters = new Parameters(factory, mapper, tester);

		parameters.setPopulationSize(1_000);
		parameters.setGenomeMultiplicity(2);
		parameters.setCrossoverStrategy(CrossoverStrategy.CrossoverGametes);
		parameters.setNumberOfParents(2);
		parameters.setElitismCount(20);
		parameters.setCrossoverOperator(new Crossover(1.0));
		parameters.setMutationOperator(new MultipleMutation(0.01));
		parameters.setPopulationFactory(new SimplePopulationFactory());
		parameters.setMaximumDiscardRatio(1.0);
		parameters.setSortPopulation(true);

		var lifeCycle = new LifeCycle(parameters);

		lifeCycle.initGeneration();

		while (lifeCycle.generation() < MAX_GENERATIONS) {

			lifeCycle.runGeneration();
			status(lifeCycle);
		}
	}

	private static void simpleCheck(WorldMap world) {

		// Start at 0

		var remaining = new HashSet<Integer>();
		for(int i = 1; i < world.count(); ++i) {
			remaining.add(i);
		}

		var route = new ArrayList<Integer>();
		route.add(0);

		int currentCity = 0;
		double totalDistance = 0;

		while (remaining.size() > 0) {
			int closest = -1;
			double distance = Double.MAX_VALUE;

			for(int r : remaining) {
				var d = world.distance(currentCity, r);
				if(d < distance) {
					closest = r;
					distance = d;
				}
			}

			totalDistance += distance;

			route.add(closest);
			remaining.remove(closest);
			currentCity = closest;
		}

		int lastCity = route.get(route.size() - 1);
		totalDistance += world.distance(lastCity, 0);

		System.out.println("Best guess: distance=" + totalDistance);
		System.out.println("Best guess: route=" + route);
	}

	private static double previousDistance = Double.MAX_VALUE;

	private static void status(LifeCycle lifeCycle) {

		var currentPopulation = lifeCycle.getCurrentPopulation();

		var c = currentPopulation.getMember(0).chromosomes()[0][0];
		int[] route = (int[]) c.alleles();

		double distance = (Double) currentPopulation.getFitness(0).value();
		if(distance != previousDistance) {
			previousDistance = distance;
			System.out.print(lifeCycle.generation() + ": d= " + distance + "  ");

			for(int i = 0; i < route.length; ++i)
				System.out.print(route[i] + ".");
			System.out.println();
		}
	}

}
