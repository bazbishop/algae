package satisfiability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import algae.*;
import algae.chromosome.*;
import algae.fitness.*;
import algae.operators.crossover.MultipleCrossover;
import algae.operators.mutation.MultipleMutation;
import algae.population.UniquePopulationFactory;

/**
 * Solve a propositional satisfiability problem. 
 */
public class PropositionalSatisfiability {
	
	/**
	 * Phenotype mapper that assigns values to each of the variables
	 * based on values encoded in the genome. 
	 */
	private static class Mapper implements IPhenotypeMapper {
		public Mapper(Conjunction conjunction) {
			mVariables = conjunction.getVariables();
		}

		@Override
		public Map<String, Boolean> createPhenotype(Genome genome) {
			// Calculate the variable assignments
			return computeMapping(mVariables, genome, 0);
		}

		final Set<String> mVariables;
	}

	/**
	 * Fitness tester that takes the variable assignment and checks each
	 * disjunction for correctness.
	 */
	private static class Tester implements IFitnessTester {
		public Tester(Conjunction conjunction) {
			mConjunction = conjunction;
		}

		final Conjunction mConjunction;

		@Override
		public IFitness fitness(Object phenotype) {
			@SuppressWarnings("unchecked")
			Map<String, Boolean> mapping = (Map<String, Boolean>) phenotype;
			int bestError = Integer.MAX_VALUE;

			// Calculate the variable assignments
			// Measure the fitness
			int error = 0;
			for (Disjunction d : mConjunction.mDisjunctions) {
				error += score(d, mapping);
			}

			if (error < bestError)
				bestError = error;

			return new IntegerFitness(-bestError, bestError == 0);
		}

		int score(Disjunction disjunction, Map<String, Boolean> mapping) {
			List<Term> terms = disjunction.mTerms;
			int size = terms.size();

			for (Term term : terms) {
				boolean value = mapping.get(term.variableName);

				if (term.positive && value)
					return 0;

				if (!term.positive && !value)
					return 0;
			}

			return size; // Failed
		}
	}

	private static Map<String, Boolean> computeMapping(Set<String> variables, Genome genome, int chromosomeIndex) {
		IChromosome[] chromosomes = genome.chromosomes()[0];

		var ch = (BitSetChromosome) chromosomes[chromosomeIndex];

		assert ch.length() == variables.size();

		// Calculate the variable assignments
		Map<String, Boolean> mapping = new HashMap<String, Boolean>();

		int a = 0;
		for (String variable : variables) {
			mapping.put(variable, ch.alleles().get(a++));
		}
		return mapping;
	}

	// Formula parameters (take care not to specify something that can never be satisfied).
	private static final int NUM_VARIABLES = 120;
	private static final int LITERALS_PER_CLAUSE = 3;
	private static final int NUM_CLAUSES = 16000;
	
	private static final int MAX_GENERATIONS = 1000;

	public static void main(String[] args) {
		System.out.println("Creating formula...");

		FormulaFactory factory = new FormulaFactory(NUM_VARIABLES, LITERALS_PER_CLAUSE, NUM_CLAUSES);
		String expression = factory.makeFormula();

		System.out.println("Attempting to solve: ");
		System.out.println(expression);
		
		Parser parser = new Parser();
		Conjunction conjunction = parser.parse(expression);

		int numVariables = conjunction.getVariables().size();

		var parameters = new Parameters(new BitSetChromosomeFactory(numVariables), new Mapper(conjunction), new Tester(conjunction));
		
		parameters.setPopulationSize(1000);
		parameters.setGenomeMultiplicity(2);
		parameters.setCrossoverStrategy(CrossoverStrategy.CrossoverGametes);
		parameters.setNumberOfParents(2);
		parameters.setCrossoverOperator(new MultipleCrossover(0.02));
		parameters.setMutationOperator(new MultipleMutation(0.01));
		parameters.setPopulationFactory(new UniquePopulationFactory());
		parameters.setMaximumDiscardRatio(2.0);

		var lifeCycle = new LifeCycle(parameters);

		boolean finished = lifeCycle.initGeneration();

		while (!finished) {
			finished = lifeCycle.runGeneration();

			System.out.print('.');
			
			if (lifeCycle.generation() > MAX_GENERATIONS)
				break;
		}
		System.out.println();

		boolean solved = lifeCycle.isFinished();

		if (solved)
			System.out.println("**** SOLVED ****");
		else
			System.out.println("Not solved");
			
		System.out.println("Generations=" + lifeCycle.generation());

		var pop = lifeCycle.getCurrentPopulation();

		Set<Map<String, Boolean>> uniqueSolutions = new HashSet<Map<String, Boolean>>();
		//Set<Map<String, Boolean>> goodSolutions = new HashSet<Map<String, Boolean>>();
		for (int m = 0; m < pop.size(); ++m) {
			var member = pop.getMember(m);
			var fitness = (IntegerFitness) pop.getFitness(m);
			Map<String, Boolean> mapping = computeMapping(conjunction.getVariables(), member, 0);

			if (fitness.value() == 0) {
				if (uniqueSolutions.add(mapping))
					System.out.println("Satified with: " + mapping);
			}
			//else {
			//		goodSolutions.add(mapping);
			//	if (m > 10)
			//		break;
			//
			//	System.out.println("Scored: " + fitness.value() + " with: " + mapping);
			//}
		}
	}
}
