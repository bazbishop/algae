package baz.algae;

public interface IFitness extends Comparable<IFitness> {
	/** Indicates that the solution is correct. */
	boolean isFinished();
}
