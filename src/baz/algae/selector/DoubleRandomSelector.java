package baz.algae.selector;

public class DoubleRandomSelector extends RandomSelector {
	public DoubleRandomSelector() {
		super( 2, 0 );
	}

	public DoubleRandomSelector( int range ) {
		super( 2, range );
	}
}
