package baz.sat;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import baz.algae.GroupedLifeCycle;
import baz.algae.IFitness;
import baz.algae.IFitnessTester;
import baz.algae.IGenome;
import baz.algae.IPhenotypeMapper;
import baz.algae.Member;
import baz.algae.chromosome.CrossoverEngine;
import baz.algae.chromosome.IChromosome;
import baz.algae.chromosome.IntegerChromosome;
import baz.algae.chromosome.IntegerChromosomeFactory;
import baz.algae.chromosome.poly.PolyGenome;
import baz.algae.chromosome.poly.PolyGenomeFactory;
import baz.algae.fitness.IntegerFitness;
import baz.algae.selector.DoubleRandomSelector;

public class PropositionalSatisfiability
{
	static class Mapper implements IPhenotypeMapper {
		public Mapper(Conjunction conjunction) {
			mVariables = conjunction.getVariables();
		}
		
		public Map<String,Boolean> createPhenotype(IGenome genome) {
			// Calculate the variable assignments
			 return computeMapping( mVariables, genome, 0 );
		}
		final Set<String> mVariables;
	}
	
	static class Tester implements IFitnessTester
	{
		public Tester( Conjunction conjunction )
		{
			mConjunction = conjunction;
		}

		final Conjunction mConjunction;

		public IFitness fitness( Object phenotype )
		{
			Map<String,Boolean> mapping = (Map<String,Boolean>) phenotype;
			int bestError = 99999999;

			// Calculate the variable assignments
			// Measure the fitness
			int error = 0;
			for( Disjunction d : mConjunction.mDisjunctions )
			{
				error += score( d, mapping );
			}
			
			if( error < bestError )
				bestError = error;

			return new IntegerFitness( bestError, bestError == 0 );
		}
		
		int score( Disjunction disjunction, Map<String,Boolean> mapping )
		{
			List<Term> terms = disjunction.mTerms;
			int size = terms.size();
			
			for( Term term : terms )
			{
				boolean value = mapping.get( term.variableName );
				 
				if( term.positive && value )
					return 0;

				if( ! term.positive && ! value )
					return 0;
			}
			
			return size; // Failed
		}
	}

	static Map<String,Boolean> computeMapping( Set<String> variables, IGenome genome, int chromosomeIndex )
	{
		IChromosome[] chromosomes = ((PolyGenome) genome).chomosomes();

		IntegerChromosome ch = (IntegerChromosome) chromosomes[ chromosomeIndex ];
		
		assert ch.length() == variables.size();

		// Calculate the variable assignments
		Map<String,Boolean> mapping = new HashMap<String,Boolean>();

		int a = 0;
		for( String variable : variables )
		{
			mapping.put( variable, ch.mAlleles[ a++ ] != 0 );
		}
		return mapping;
	}
	
	private static IntegerChromosomeFactory chromosomeFactory;
	private static PolyGenomeFactory genomeFactory;
	private static Tester tester;
	private static Mapper mapper;

//	static final int NUM_VARIABLES = 700;
//	static final int LITERALS_PER_CLAUSE = 3;
//	static final int NUM_CLAUSES = 2975;

	static final int NUM_VARIABLES = 70;
	static final int LITERALS_PER_CLAUSE = 3;
	static final int NUM_CLAUSES = 297;
	
	public static void main( String[] args )
	{
		FormulaFactory factory = new FormulaFactory( NUM_VARIABLES, LITERALS_PER_CLAUSE, NUM_CLAUSES );
		String expression = factory.makeFormula();
		
		System.out.println( "Checking stisfiability of: " );
		System.out.println( expression );
		Parser parser = new Parser();
		Conjunction conjunction = parser.parse( expression );
		
		int numVariables = conjunction.getVariables().size();

		chromosomeFactory = new IntegerChromosomeFactory( numVariables, 1, 0 );

		genomeFactory = new PolyGenomeFactory(
						2, // numParentsPerChild
		                2, // numChromosomesPerIndividual,
		                chromosomeFactory,
		                new CrossoverEngine(	chromosomeFactory,
		                						0.02,	// cross-over chance per allele
		                						0.0 )	// mutation chance per allele
						);
		
		mapper = new Mapper( conjunction );
		tester = new Tester( conjunction );

		final int NUM_GROUPS = 10;
		final int MAX_GENERATIONS = 50;
		final int POPULATION_SIZE = 1000;
		final int MAX_GROUP_CYCLES = 10;
		
		GroupedLifeCycle groupedCycle = new GroupedLifeCycle(
						POPULATION_SIZE,
						MAX_GENERATIONS,
						mapper,
						tester,
						genomeFactory,
						new DoubleRandomSelector(),
						1, // elitismCount,
		                NUM_GROUPS,
		                MAX_GROUP_CYCLES );
		
		boolean solved = groupedCycle.run();
		
		if( solved )
			System.out.println( "**** SOLVED ****" );
		
		List<Member> pop = groupedCycle.getBestGroupedPopulation();
		
		Set<Map<String,Boolean>> uniqueSolutions = new HashSet<Map<String,Boolean>>();
		Set<Map<String,Boolean>> goodSolutions = new HashSet<Map<String,Boolean>>();
		for( int m = 0; m < POPULATION_SIZE; ++m )
		{
			Member member = pop.get( m );
			IntegerFitness fitness = (IntegerFitness) member.fitness;
			Map<String,Boolean> mapping = computeMapping( conjunction.getVariables(), member.genome, 0 );

			if( fitness.mValue == 0 )
			{
				if( uniqueSolutions.add( mapping ) )
					System.out.println( "Satified with: " + mapping );
			}
			else
			{
				goodSolutions.add( mapping );
				if( m > 10 )
					break;
				
				System.out.println( "Scored: " + fitness.mValue + " with: " + mapping );
			}
		}
		
	}
	
}
