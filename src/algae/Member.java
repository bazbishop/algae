package algae;

public class Member implements Comparable<Member> {
	
	public IFitness fitness;

	public final Genome genome;

	public Member( Genome g ) {
		genome = g;
	}

	public int compareTo( Member rhs ) {
		return fitness.compareTo( rhs.fitness );
	}
}
