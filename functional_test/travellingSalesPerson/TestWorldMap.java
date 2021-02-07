package travellingSalesPerson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestWorldMap {

	@BeforeEach
	void setup() {

		var x = new double[] { 1.0, 5.0, 5.0 };
		var y = new double[] { 1.0, 1.0, 4.0 };

		pythagoras = new WorldMap(x, y);
	}

	private WorldMap pythagoras;

	@Test
	void testDistance() {

		assertEquals(0.0, pythagoras.distance(0, 0));
		assertEquals(0.0, pythagoras.distance(1, 1));
		assertEquals(0.0, pythagoras.distance(2, 2));

		assertEquals(3.0, pythagoras.distance(1, 2));
		assertEquals(4.0, pythagoras.distance(0, 1));
		assertEquals(5.0, pythagoras.distance(0, 2));

		assertEquals(3.0, pythagoras.distance(2, 1));
		assertEquals(4.0, pythagoras.distance(1, 0));
		assertEquals(5.0, pythagoras.distance(2, 0));
	}

	@Test
	void testClosest() {

		assertEquals(1, pythagoras.closest(0));
		assertEquals(2, pythagoras.closest(1));
		assertEquals(1, pythagoras.closest(2));
	}

}
