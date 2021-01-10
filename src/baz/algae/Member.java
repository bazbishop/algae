package baz.algae;

public class Member implements Comparable<Member> {
	public IFitness fitness;

	public final IGenome genome;

	public Member( IGenome g ) {
		genome = g;
	}

	public int compareTo( Member rhs ) {
		return fitness.compareTo( rhs.fitness );
	}
}
