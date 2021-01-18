package algae;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A container for Member instances.
 */
public class Population {

	/**
	 * Constructor.
	 * 
	 * @param intendedSize The number of members to be managed by this Population
	 *                     instance
	 */
	public Population(int intendedSize) {
		members = new ArrayList<Member>(intendedSize);
	}

	public void addMember(Genome genome) {
		members.add(new Member(genome));
	}

	public Genome getMember(int memberIndex) {
		return members.get(memberIndex).genome();
	}

	public void setFitness(int memberIndex, IFitness fitness) {
		members.get(memberIndex).setFitness(fitness);
	}

	public IFitness getFitness(int memberIndex) {
		return members.get(memberIndex).fitness();
	}

	public int size() {
		return members.size();
	}

	public void sort() {
		Collections.sort(members, Collections.reverseOrder());
	}

	public void take(Population targetPopulation, int survivors) {

		int count = Math.min(survivors, members.size());
		for (int i = 0; i < count; ++i) {
			targetPopulation.members.add(members.get(i));
		}
	}

	private final List<Member> members;
}
