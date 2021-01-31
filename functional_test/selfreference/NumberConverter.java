package selfreference;

public class NumberConverter {

	private static final String[] g_units = new String[] { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
	private static final String[] g_teens = new String[] { "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };
	private static final String[] g_tens = new String[] { "zero", "ten", "twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninety", };

	private static final String space = " ";
	private static final String and = "and ";
	private static final String hundred = " hundred ";

	/**
	 * 9,223,372,036,854,775,807
	 * @param n
	 * @return
	 */
	public String convert(int n) {
		if (n == 0)
			return "zero";

		int billions = n / 1_000_000_000;
		int remainder = n % 1_000_000_000;
		
		int millions = remainder / 1_000_000;
		remainder = remainder % 1_000_000;
		
		int thousands = remainder / 1_000;
		remainder = remainder % 1_000;
		

		StringBuilder result = new StringBuilder();

		if (billions > 0)
			result.append(convert_0_999(billions, false)).append(" billion ");

		if (millions > 0)
			result.append(convert_0_999(millions, true)).append(" million ");

		if (thousands > 0)
			result.append(convert_0_999(thousands, true)).append(" thousand ");

//		if (remainder > 0)
			result.append(convert_0_999(remainder, false));

		return result.toString();
	}

	private String convert_0_999(int n, boolean precedingAnd) {
		assert n >= 0 && n < 1000;

		StringBuilder result = new StringBuilder();

//			if ( n == 0 )
//				return g_units[ 0 ];

		int hundreds = (n / 100);
		int remainder = n % 100;
		
		int tens = remainder / 10;
		int units = remainder % 10;

		if (hundreds > 0) {
			result.append(g_units[hundreds]).append(hundred);
		}
		
		if (hundreds > 0 || precedingAnd) {
			if (tens > 0 || units > 0)
				result.append(and);
		}

		if (tens == 0) {
			if (hundreds == 0 || units > 0) {
				result.append(g_units[units]);
			}
		} else if (tens == 1) {
			result.append(g_teens[units]).append(space);
		} else if (tens > 1) {
			result.append(g_tens[tens]).append(space);

			if (units > 0) {
				result.append(g_units[units]);
			}
		}

		return result.toString();
	}
	
	public static void main(String[] args) {
		
		var c = new NumberConverter();
		
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
	}
	
	private static void show(int n) {
		var c = new NumberConverter();
		var r = c.convert(n);
		System.out.println("" + n + " => " + r);
	}
}
