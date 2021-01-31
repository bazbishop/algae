Introduction
============

A propositional formula is a logical expression of boolean variables with operators:

	unary negation - 'not'
	disjunction - 'or'
	conjunction - 'and'

all such expressions can be reduced to a single conjunction of disjunctions
and this is the form used as input to this example program.

The first step is to use the FormulaFactory to create a formula that is
actually solvable. Care must be taken with the parameters, because the formula
is generated at random and if the parameters make it impossible to create
enough disjunctions then the factory will enter an infinite loop.

The next step is to parse the formula and start mapping the bit set chromosome
values into variables.   
