/**
 * 
 */
package baz.sat;

public class Term
{
	public Term( String variableName, boolean positive )
	{
		this.variableName = variableName;
		this.positive = positive;
	}
	
	public String toString()
	{
		if( positive )
			return variableName;
		else
			return "!" + variableName;
	}
	
	final String variableName;
	final boolean positive;
}