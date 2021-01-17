package baz.sat;

import java.util.Random;

public class FormulaFactory {
	public FormulaFactory(int numVariables, int literalsPerClause, int numClauses) {
		mRandom = new Random();

		mNumVariables = numVariables;
		mLiteralsPerClause = literalsPerClause;
		mNumClauses = numClauses;
	}

	public String makeFormula() {
		StringBuilder result = new StringBuilder();

		for (int clause = 0; clause < mNumClauses; ++clause) {
			result.append("[");

			int previousVarIndex = -1;

			boolean first = true;
			for (int term = 0; term < mLiteralsPerClause; ++term) {
				if (first)
					first = false;
				else
					result.append(',');

				if (mRandom.nextInt(2) == 0)
					result.append("!");

				int varIndex;
				do {
					varIndex = mRandom.nextInt(mNumVariables);
				} while (varIndex == previousVarIndex);

				result.append(makeVar(varIndex));

				previousVarIndex = varIndex;
			}

			result.append("]");
		}

		return result.toString();
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

	private final Random mRandom;
}
