package algae.selector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestRandomSelector {

	@Test
	public void testSelect() {
		final int sectors = 10;
		final int repetitions = 10000;
		final int range = 1000;

		var selector = new RandomSelector();

		var counters = new int[sectors];

		for (int i = 0; i < repetitions; ++i) {
			int v = selector.select(range);
			assertTrue(v >= 0);
			assertTrue(v < range);

			for (int s = 0; s < sectors; ++s) {
				if (v < range * (s + 1) / sectors) {
					++counters[s];
					break;
				}
			}
		}

		for (int s = 1; s < sectors; ++s) {
			assertTrue(counters[s - 1] > counters[s]);
		}
	}
}
