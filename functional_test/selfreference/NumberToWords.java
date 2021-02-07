package selfreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert a long value into english words using the american of british style (which affects the use of 'and').
 */
public class NumberToWords {

	/**
	 * The language style
	 */
	public enum Style {
		/**
		 * Avoid using 'and', e.g. 708 => seven hundred eight
		 */
		AMERICAN,

		/**
		 * Use 'and', e.g. 708 => seven hundred and eight
		 */
		BRITISH
	};

	/**
	 * Constructor.
	 * @param style The preferred language style
	 */
	public NumberToWords(Style style) {
		this.style = style;
	}

	/**
	 * Do the conversion.
	 * @param n The number to convert - the full range of 'long' is allowed
	 * @return The value of parameter 'n' in words
	 */
	public String convert(long n) {

		if (n == 0) {
			return zero;
		}

		var tokens = new ArrayList<String>();

		if (n < 0) {
			tokens.add(minus);
			n = -n;
		}

		int[] groups = new int[tenPower3nNames.length];

		for (int g = 0; g < groups.length; ++g) {
			groups[g] = (int) (n % 1000);
			n /= 1000;
		}

		boolean finalAnd = false;

		for (int g = groups.length - 1; g >= 0; --g) {
			if (groups[g] > 0) {

				if (g > 0)
					finalAnd = true;

				convert_1_999(tokens, groups[g], g > 0 ? false : finalAnd);

				if (g > 0)
					tokens.add(tenPower3nNames[g]);
			}
		}

		return String.join(" ", tokens);
	}

	private void convert_1_999(List<String> tokens, int n, boolean finalAnd) {
		assert n >= 1 && n <= 999;

		int hundreds = (n / 100);
		int remainder = n % 100;

		int tens = remainder / 10;
		int units = remainder % 10;

		if (hundreds > 0) {
			tokens.add(unitNames[hundreds]);
			tokens.add(hundred);
		}

		if (style == Style.BRITISH) {
			if (hundreds > 0 || finalAnd) {
				if (tens > 0 || units > 0)
					tokens.add(and);
			}
		}

		if (tens == 0) {
			if (hundreds == 0 || units > 0) {
				tokens.add(unitNames[units]);
			}
		}
		else if (tens == 1) {
			tokens.add(teenNames[units]);
		}
		else if (tens > 1) {
			tokens.add(tenNames[tens]);

			if (units > 0) {
				tokens.add(unitNames[units]);
			}
		}
	}

	private static final String[] unitNames = new String[] { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
	private static final String[] teenNames = new String[] { "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen",
			"nineteen" };
	private static final String[] tenNames = new String[] { "zero", "ten", "twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninety", };
	private static final String[] tenPower3nNames = new String[] { "unit", "thousand", "million", "billion", "trillion", "quadrillion", "quintillion",
			"sextillion" };

	/*
	 * If extending this class for BigInteger, the following large numbers can be added to tenPower3nNames, which go from
	 * 10^24 (septillion) to 10^63 (vigintillion) septillion octillion nonillion decillion undecillion duodecillion
	 * tredecillion quattuordecillion quindecillion sexdecillion septendecillion octodecillion novemdecillion vigintillion
	 */

	private static final String minus = "minus";
	private static final String zero = "zero";
	private static final String and = "and";
	private static final String hundred = "hundred";

	private Style style;

	public static void main(String[] args) {

		show(0);
		show(1);
		show(2);
		show(9);
		show(10);
		show(11);
		show(19);
		show(20);
		show(21);
		show(22);
		show(29);
		show(30);
		show(31);
		show(99);
		show(100);
		show(101);
		show(102);
		show(110);
		show(111);
		show(112);
		show(119);
		show(120);
		show(121);
		show(199);
		show(450);
		show(900);
		show(999);
		show(1000);
		show(1006);
		show(1010);
		show(1018);
		show(1020);
		show(1029);
		show(1030);
		show(100000);
		show(2000000);
		show(30000000);
		show(2_123_456_789);
		show(1_101_002_030_400_999_020L);
	}

	private static void show(long n) {
		var c = new NumberToWords(Style.BRITISH);
		var r = c.convert(n);
		System.out.println("" + n + " => " + r);
	}

}
