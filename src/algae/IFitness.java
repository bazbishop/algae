package algae;

/**
 * The computed fitness of a member based on its genome. 
 */
public interface IFitness extends Comparable<IFitness> {
	/** Indicates that the solution is correct/optimal and cannot be improved. */
	boolean isOptimal();
}
