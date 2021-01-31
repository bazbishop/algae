package satisfiability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import algae.util.Rand;

/**
 * Make a satisfiable propositional formula made up of a conjunction (logical AND)
 * of many disjunctions (logical OR). Each disjunction contains a boolean variables
 * each with a possible negation operator (!).
 */
public class FormulaFactory {
	
	/**
	 * Constructor
	 * @param numVariables The total number of boolean variables to use
	 * @param literalsPerClause The number of literal (variables) per clause (disjunction)
	 * @param numClauses The number of clauses (disjunctions)
	 */
	public FormulaFactory(int numVariables, int literalsPerClause, int numClauses) {

		mNumVariables = numVariables;
		mLiteralsPerClause = literalsPerClause;
		mNumClauses = numClauses;
	}
	
	/**
	 * Make a formula.
	 * @return A formula of the form [a,b,!c][!d,!e,f]... 
	 */
	public String makeFormula() {
		
		var variables = createValues();
		
		var formula = new ArrayList<List<SignedTerm>>();
		
		while(formula.size() < mNumClauses) {
			
			var disjunction = makeRandomDisjunction(variables);
			if(evaluateDisjunction(disjunction, variables)) {
				if(!contains(formula, disjunction)) {
					formula.add(disjunction);
				}
			}
		}

		StringBuilder result = new StringBuilder();

		for (var disjunction : formula) {
			result.append("[");

			boolean first = true;
			for (var term : disjunction) {
				if (first)
					first = false;
				else
					result.append(',');

				if(!term.positive)
					result.append('!');
				
				result.append(makeVar(term.variable));
			}

			result.append("]");
		}

		return result.toString();
	}

	class SignedTerm {
		SignedTerm(int variable, boolean positive) {
			this.variable = variable;
			this.positive = positive;
		}
		final int variable;
		final boolean positive;
	}
	
	private boolean evaluateDisjunction(List<SignedTerm> terms, List<Boolean> variables) {
		boolean result = false;
		
		for(var term : terms) {
			if(variables.get(term.variable) && term.positive)
				return true;
			else if(! variables.get(term.variable) && ! term.positive)
				return true;
		}		
		
		return result;
	}
	
	private List<SignedTerm> makeRandomDisjunction(List<Boolean> variables) {
		var terms = new ArrayList<SignedTerm>();
		
		while(terms.size() < mLiteralsPerClause)
		{
			int variable = Rand.nextInt(mNumVariables);
			if(! contains(terms, variable)) {
				terms.add(new SignedTerm(variable, Rand.nextBoolean()));
			}
		}

		Collections.sort(terms, new Comparator<SignedTerm>() {
			@Override
			public int compare(SignedTerm o1, SignedTerm o2) {
				return Integer.compare(o1.variable, o2.variable);
			}});
		
		return terms;
	}

	// Check if a variable is in a disjunction
	boolean contains(List<SignedTerm> disjunction, int variable) {
		for(int t = 0; t < disjunction.size(); ++t) {
			if(disjunction.get(t).variable == variable)
				return true;
		}
		return false;
	}
	
	// Check if a disjunction is in a formula
	private boolean contains(ArrayList<List<SignedTerm>> formula, List<SignedTerm> disjunction) {
		
		for(var d : formula) {
			boolean same = true;
			for(int i = 0; i < d.size(); ++i) {
				
				var fterm = d.get(i);
				var gterm = disjunction.get(i);
				
				if(fterm.variable != gterm.variable)
					same = false;
				if(fterm.positive != gterm.positive)
					same = false;
			}
			
			if(same)
				return true;
		}

		return false;
	}

	private List<Boolean> createValues() {
		var variables = new ArrayList<Boolean>();
		
		for(int v = 0; v < mNumVariables; ++v) {
			variables.add(Rand.nextBoolean());
		}
		
		return variables;
	}

	private String makeVar(int n) {
		StringBuilder result = new StringBuilder();

		final int range = 26;

		int d1 = n % range;
		n /= range;
		int d2 = n % range;
		n /= range;
		int d3 = n % range;

		if (d3 > 0)
			result.append(toChar('A' + d3));

		if (d2 > 0 || d3 > 0)
			result.append(toChar('A' + d2));

		result.append(toChar('A' + d1));

		return result.toString();
	}

	private char toChar(int ch) {
		return (char) ch;
	}

	private final int mNumVariables;
	private final int mLiteralsPerClause;
	private final int mNumClauses;
}
