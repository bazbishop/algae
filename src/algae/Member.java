package algae;

/**
 * An individual in a population, which has a genome and a fitness after
 * measurement.
 */
public class Member implements Comparable<Member> {

	/**
	 * Constructor for before fitness testing.
	 */
	public Member(Genome genome) {
		assert genome != null;

		mGenome = genome;
		mFitness = null;
	}

	/**
	 * Constructor for after fitness testing.
	 */
	public Member(Genome genome, IFitness fitness) {
		assert genome != null;
		assert fitness != null;

		mGenome = genome;
		mFitness = fitness;
	}

	public int compareTo(Member rhs) {
		return mFitness.compareTo(rhs.mFitness);
	}

	/**
	 * Get the fitness.
	 * @return The fitness value or null if it hasn't been tested yet.
	 */
	public IFitness fitness() {
		return mFitness;
	};

	/**
	 * Get the member's genome.
	 * @return The genome.
	 */
	public Genome genome() {
		return mGenome;
	}

	private final IFitness mFitness;
	private final Genome mGenome;
}
