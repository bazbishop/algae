/**
 * 
 */
package baz.sat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Conjunction
{
	public void add( Disjunction disjunction )
	{
		mDisjunctions.add( disjunction );
	}
	
	public Set<String> getVariables()
	{
		Set<String> result = new HashSet<String>();
		for( Disjunction disjunction : mDisjunctions )
			result.addAll( disjunction.getVariables() );
		
		return result;
	}
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		for( Disjunction disjunction : mDisjunctions )
			result.append( disjunction );
		
		return result.toString();
	}
	
	final List<Disjunction> mDisjunctions = new ArrayList<Disjunction>();
}