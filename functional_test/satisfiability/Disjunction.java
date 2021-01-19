/**
 * 
 */
package satisfiability;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Disjunction {
	public void add(Term term) {
		mTerms.add(term);
	}

	public Set<String> getVariables() {
		Set<String> result = new HashSet<String>();
		for (Term term : mTerms)
			result.add(term.variableName);

		return result;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append('[');

		boolean first = true;
		for (Term term : mTerms) {
			if (first)
				first = false;
			else
				result.append(',');
			result.append(term);
		}
		result.append(']');

		return result.toString();
	}

	final List<Term> mTerms = new ArrayList<Term>();
}