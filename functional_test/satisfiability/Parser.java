package satisfiability;

import java.util.StringTokenizer;

public class Parser {
	public Parser() {
	}

	public Conjunction parse(String expression) {
		Conjunction result = new Conjunction();

		StringTokenizer c = new StringTokenizer(expression, "[]");

		while (c.hasMoreTokens()) {
			Disjunction disjunction = new Disjunction();

			String disjunctionExpr = c.nextToken();

			StringTokenizer d = new StringTokenizer(disjunctionExpr, ",");

			while (d.hasMoreTokens()) {
				String literal = d.nextToken().trim();
				boolean positive;
				String variable;

				if (literal.startsWith("!")) {
					positive = false;
					variable = literal.substring(1).trim();
				}
				else {
					positive = true;
					variable = literal;
				}

				disjunction.add(new Term(variable, positive));
			}

			result.add(disjunction);
		}

		return result;
	}
}
