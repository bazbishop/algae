package algae.population;

import algae.Genome;
import algae.IFitness;

/**
 * An individual in a population, which has a genome and a fitness after
 * measurement.
 */
class Member implements Comparable<Member> {

	/**
	 * Constructor for before fitness testing.
	 */
	public Member(Genome genome) {
		assert genome != null;

		mGenome = genome;
		mFitness = null;
	}

	@Override
	public int hashCode() {
		return 37 + mGenome.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;
		
		if (!(obj instanceof Member))
			return false;
		
		Member other = (Member) obj;
		
		return mGenome.equals(other.mGenome);
	}

	public int compareTo(Member rhs) {
		return mFitness.compareTo(rhs.mFitness);
	}

	/**
	 * Get the fitness.
	 * 
	 * @return The fitness value or null if it hasn't been tested yet.
	 */
	public IFitness fitness() {
		return mFitness;
	};

	/**
	 * Set the fitness of this member.
	 * 
	 * @param fitness The fitness implementation
	 */
	public void setFitness(IFitness fitness) {
		mFitness = fitness;
	}

	/**
	 * Get the member's genome.
	 * 
	 * @return The genome.
	 */
	public Genome genome() {
		return mGenome;
	}

	private IFitness mFitness;
	private final Genome mGenome;
}
