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
	 * @param intendedSize The number of members to be managed by this Population instance
	 */
	public Population(int intendedSize) {
		this.intendedSize = intendedSize;
		members = new ArrayList<Member>(intendedSize);
	}
	
	public void add(Member member) {
		members.add(member);
	}

	public Member get(int memberIndex) {
		return members.get(memberIndex);
	}
	
	public int size() {
		return members.size();
	}
	
	public void sort() {
		Collections.sort(members, Collections.reverseOrder());
	}
	
	public Population take(int survivors) {
		var result = new Population(intendedSize);
		
		int count = Math.min(survivors, members.size());
		for(int i = 0; i < count; ++i)
		{
			result.members.add(members.get(i));
		}		

		return result;
	}

	private final List<Member> members;
	private final int intendedSize;
}
