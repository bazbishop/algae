package baz.sat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import algae.*;
import algae.chromosome.*;
import algae.fitness.*;
import algae.population.*;
import algae.selector.*;

public class PropositionalSatisfiability {
	static class Mapper implements IPhenotypeMapper {
		public Mapper(Conjunction conjunction) {
			mVariables = conjunction.getVariables();
		}

		public Map<String, Boolean> createPhenotype(Genome genome) {
			// Calculate the variable assignments
			return computeMapping(mVariables, genome, 0);
		}

		final Set<String> mVariables;
	}

	static class Tester implements IFitnessTester {
		public Tester(Conjunction conjunction) {
			mConjunction = conjunction;
		}

		final Conjunction mConjunction;

		public IFitness fitness(Object phenotype) {
			@SuppressWarnings("unchecked")
			Map<String, Boolean> mapping = (Map<String, Boolean>) phenotype;
			int bestError = 99999999;

			// Calculate the variable assignments
			// Measure the fitness
			int error = 0;
			for (Disjunction d : mConjunction.mDisjunctions) {
				error += score(d, mapping);
			}

			if (error < bestError)
				bestError = error;

			return new IntegerFitness(bestError, bestError == 0);
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

	static class Parameters implements IParameters {

		@Override
		public int populationSize() {
			return 10000;
		}

		@Override
		public int elitismCount() {
			return 0;
		}

		@Override
		public double crossOverProbabilityPerAllele(int chromosomeSetIndex) {
			return 0.02;
		}

		@Override
		public double mutationProbabilityPerAllele(int chromosomeSetIndex) {
			return 0.0;
		}

		@Override
		public ISelector selector() {
			return new RandomSelector();
		}
	};

	static Map<String, Boolean> computeMapping(Set<String> variables, Genome genome, int chromosomeIndex) {
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

	private static BitSetChromosomeFactory chromosomeFactory;

	static final int NUM_VARIABLES = 10;
	static final int LITERALS_PER_CLAUSE = 3;
	static final int NUM_CLAUSES = 6;

	public static void main(String[] args) {
		FormulaFactory factory = new FormulaFactory(NUM_VARIABLES, LITERALS_PER_CLAUSE, NUM_CLAUSES);
		String expression = factory.makeFormula();

		System.out.println("Checking satisfiability of: ");
		System.out.println(expression);
		Parser parser = new Parser();
		Conjunction conjunction = parser.parse(expression);

		int numVariables = conjunction.getVariables().size();

		chromosomeFactory = new BitSetChromosomeFactory(numVariables);

		List<IChromosomeFactory> factories = new ArrayList<IChromosomeFactory>();
		factories.add(chromosomeFactory);

		var mapper = new Mapper(conjunction);

		var lifeCycle = new LifeCycle(new Parameters(), 2, 2, new SimplePopulationFactory(),
				CrossoverStrategy.CrossoverGametes, factories, mapper, new Tester(conjunction));

		boolean finished = lifeCycle.initGeneration();

		while (!finished) {
			finished = lifeCycle.runGeneration();

			if (lifeCycle.generation() > 1000)
				break;
		}

		boolean solved = lifeCycle.isFinished();

		if (solved)
			System.out.println("**** SOLVED ****");

		var pop = lifeCycle.getCurrentPopulation();

		Set<Map<String, Boolean>> uniqueSolutions = new HashSet<Map<String, Boolean>>();
		Set<Map<String, Boolean>> goodSolutions = new HashSet<Map<String, Boolean>>();
		for (int m = 0; m < pop.size(); ++m) {
			var member = pop.getMember(m);
			var fitness = (IntegerFitness) pop.getFitness(m);
			Map<String, Boolean> mapping = computeMapping(conjunction.getVariables(), member, 0);

			if (fitness.value() == 0) {
				if (uniqueSolutions.add(mapping))
					System.out.println("Satified with: " + mapping);
			} else {
				goodSolutions.add(mapping);
				if (m > 10)
					break;

				System.out.println("Scored: " + fitness.value() + " with: " + mapping);
			}
		}

	}

}
