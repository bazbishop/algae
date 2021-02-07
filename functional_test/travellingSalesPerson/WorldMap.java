package travellingSalesPerson;

import algae.util.Rand;

public class WorldMap {
	public WorldMap(int numberOfCities) {
		assert numberOfCities >= 0;

		x = new double[numberOfCities];
		y = new double[numberOfCities];

		for(int city = 0; city < numberOfCities; ++city) {
			x[city] = Rand.nextDouble() * 100;
			y[city] = Rand.nextDouble() * 100;
		}
	}

	public WorldMap(double[] x, double[] y) {
		assert x.length == y.length;

		this.x = x.clone();
		this.y = y.clone();
	}

	public double distance(int cityA, int cityB) {
		//assert cityA != cityB;
		assert cityA >= 0 && cityA < x.length;
		assert cityB >= 0 && cityB < x.length;

		double dx = x[cityA] - x[cityB];
		double dy = y[cityA] - y[cityB];

		return Math.sqrt(dx*dx + dy*dy);
	}

	public int closest(int city) {
		assert count() >= 2;
		assert city >= 0 && city < x.length;

		double shortest = Double.MAX_VALUE;
		int closestCity = -1;

		for(int c = 0; c < x.length; ++c) {
			if(c != city) {
				double dist = distance(city, c);

				if(dist < shortest) {
					shortest = dist;
					closestCity = c;
				}
			}
		}

		return closestCity;
	}

	public int count() {
		return x.length;
	}

	private final double[] x;
	private final double[] y;
}