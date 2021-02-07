package algae.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import algae.Genome;
import algae.IFitness;
import algae.IPopulation;

/**
 * A container for Member instances.
 */
public class UniquePopulation implements IPopulation {

	/**
	 * Constructor.
	 * 
	 * @param intendedSize The number of members to be managed by this Population instance
	 */
	public UniquePopulation(int intendedSize) {
		memberList = new ArrayList<Member>(intendedSize);
		memberSet = new HashSet<Genome>(intendedSize);
	}

	@Override
	public void addMember(Genome genome) {
		var member = new Member(genome);
		if (memberSet.add(genome)) {
			memberList.add(member);
			sorted = false;
		}
		else {
			++countDiscarded;
		}
	}

	@Override
	public Genome getMember(int memberIndex) {
		return memberList.get(memberIndex).genome();
	}

	@Override
	public void setFitness(int memberIndex, IFitness fitness) {
		memberList.get(memberIndex).setFitness(fitness);
	}

	@Override
	public IFitness getFitness(int memberIndex) {
		return memberList.get(memberIndex).fitness();
	}

	@Override
	public int size() {
		return memberList.size();
	}

	@Override
	public int discarded() {
		return countDiscarded;
	}

	@Override
	public void sort() {
		Collections.sort(memberList, Collections.reverseOrder());
		sorted = true;
	}

	@Override
	public boolean isSorted() {
		return sorted;
	}

	private final List<Member> memberList;
	private final Set<Genome> memberSet;
	private int countDiscarded = 0;
	private boolean sorted = false;
}
