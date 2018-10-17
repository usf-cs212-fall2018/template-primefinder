import java.util.TreeSet;

/**
 * Finds primes.
 */
public class PrimeFinder {

	/**
	 * A terrible and naive approach to determining if a number is prime.
	 *
	 * @param number to test if prime
	 * @return true if the number is prime
	 */
	public static boolean isPrime(int number) {

		if (number < 2) {
			return false;
		}

		// Check for any divisors.
		for (int i = number - 1; i > 1; i--) {
			if ((number % i) == 0) {
				return false;
			}
		}

		// Otherwise, no divisors and number is prime.
		return true;
	}

	/*
	 * Really, this is a terrible implementation. LIKE REALLY BAD. Even looping
	 * up instead of down would have been way better. But, we need a calculation
	 * that takes a long time, so here we are. An intentionally TERRIBLE
	 * implementation of finding a prime value.
	 */

	/**
	 * Returns a collection of all primes found between the start and end values.
	 *
	 * @param start  first value to evaluate if prime
	 * @param number of values to evaluate if prime
	 * @return all prime numbers found
	 * @throws IllegalArgumentException if start is < 0 or end < start
	 */
	public static TreeSet<Integer> trialDivision(int start, int end)
			throws IllegalArgumentException {
		positiveRange(start, end);

		TreeSet<Integer> primes = new TreeSet<Integer>();

		for (int i = start; i <= end; i++) {
			if (isPrime(i)) {
				primes.add(i);
			}
		}

		return primes;
	}

	/**
	 * Private helper method for making sure the range determined by the start and
	 * end values is positive and valid.
	 *
	 * @param start starting number to find primes
	 * @param end   ending number to find primes
	 */
	private static void positiveRange(int start, int end)
			throws IllegalArgumentException {
		if (start < 0) {
			throw new IllegalArgumentException("Starting value must be greater than 0.");
		}

		if (end < start) {
			throw new IllegalArgumentException("Ending value must be greater than starting value.");
		}
	}

	/**
	 * Uses a work queue to find all primes less than or equal to the maximum
	 * value. Both the maximum value and number of threads must be a positive
	 * number greater than or equal to 1.
	 *
	 * @param max     maximum value to consider (must be positive)
	 * @param threads number of worker threads (must be positive)
	 * @return set of prime numbers less than or equal to max
	 * @throws IllegalArgumentException if start is < 0 or end < start, or if max
	 *                                  or threads are less than 1.
	 */
	public static TreeSet<Integer> findPrimes(int start, int end, int threads)
			throws IllegalArgumentException {
		positiveRange(start, end);

		if (threads < 1) {
			throw new IllegalArgumentException("Number of worker threads must be greater than 0.");
		}

		TreeSet<Integer> primes = new TreeSet<Integer>();

		// TODO Fill in the rest of this method.

		return primes;
	}

	// TODO Add additional classes or methods as needed!

}
