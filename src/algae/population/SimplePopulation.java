package algae.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algae.Genome;
import algae.IFitness;
import algae.IPopulation;

/**
 * A container for Member instances.
 */
public class SimplePopulation implements IPopulation {

	/**
	 * Constructor.
	 * 
	 * @param intendedSize The number of members to be managed by this Population
	 *                     instance
	 */
	public SimplePopulation(int intendedSize) {
		members = new ArrayList<Member>(intendedSize);
	}

	@Override
	public void addMember(Genome genome) {
		members.add(new Member(genome));
	}

	@Override
	public Genome getMember(int memberIndex) {
		return members.get(memberIndex).genome();
	}

	@Override
	public void setFitness(int memberIndex, IFitness fitness) {
		members.get(memberIndex).setFitness(fitness);
	}

	@Override
	public IFitness getFitness(int memberIndex) {
		return members.get(memberIndex).fitness();
	}

	@Override
	public int size() {
		return members.size();
	}

	@Override
	public int discarded() {
		// This population type never discards anything
		return 0;
	}

	@Override
	public void sort() {
		Collections.sort(members, Collections.reverseOrder());
	}

	private final List<Member> members;
}
