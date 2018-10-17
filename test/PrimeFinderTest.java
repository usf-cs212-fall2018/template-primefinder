import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class PrimeFinderTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(60);

	public static final int WARMUP_ROUNDS = 10;
	public static final int TIMED_ROUNDS = 20;

	private static final Integer[] primes = new Integer[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53,
			59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167,
			173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281,
			283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419,
			421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557,
			563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677,
			683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827,
			829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977,
			983, 991, 997 };

	@Test
	public void testTrialDivision() {
		TreeSet<Integer> expected = new TreeSet<Integer>(Arrays.asList(primes));
		TreeSet<Integer> actual = PrimeFinder.trialDivision(1, 1000);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testFindPrimes1Thread() {
		TreeSet<Integer> expected = new TreeSet<Integer>(Arrays.asList(primes));
		TreeSet<Integer> actual = PrimeFinder.findPrimes(1, 1000, 1);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testFindPrimes2Thread() {
		TreeSet<Integer> expected = new TreeSet<Integer>(Arrays.asList(primes));
		TreeSet<Integer> actual = PrimeFinder.findPrimes(1, 1000, 2);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testFindPrimes5Thread() {
		TreeSet<Integer> expected = new TreeSet<Integer>(Arrays.asList(primes));
		TreeSet<Integer> actual = PrimeFinder.findPrimes(1, 1000, 5);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSingleVersusMulti() {
		int max = 3000;
		int threads = 5;

		TreeSet<Integer> expected = PrimeFinder.trialDivision(0, max);
		TreeSet<Integer> actual = PrimeFinder.findPrimes(1, max, threads);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void benchmarkSingleVersusMulti() {
		int max = 5000;
		int threads = 5;

		double single = new SingleBenchmarker().benchmark(max);
		double multi = new MultiBenchmarker(threads).benchmark(max);

		String debug = String.format("Single: %.4f Multi: %.4f, Speedup: %.4fx", single, multi, single / multi);
		Assert.assertTrue(debug, single >= multi);

		System.out.println(debug);
	}

	@Test
	public void benchmarkOneVersusThree() {
		int max = 5000;

		double multi1 = new MultiBenchmarker(1).benchmark(max);
		double multi3 = new MultiBenchmarker(3).benchmark(max);

		String debug = String.format("1 Thread: %.4f 3 Threads: %.4f, Speedup: %.4fx", multi1, multi3, multi1 / multi3);
		Assert.assertTrue(debug, multi1 > multi3);

		System.out.println(debug);
	}

	@Test(timeout = 200)
	public void testWorkQueue() throws InterruptedException {
		WorkQueue queue = new WorkQueue();
		CountDownLatch count = new CountDownLatch(10);

		for (int i = 0; i < 10; i++) {
			queue.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(10);
						count.countDown();
					}
					catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}
			});
		}

		queue.finish();
		queue.shutdown();

		// if you get stuck here then finish() isn't working
		count.await();
	}

	private static abstract class Benchmarker {

		public abstract void run(int max);

		public double benchmark(int max) {
			// warmup
			for (int i = 0; i < WARMUP_ROUNDS; i++) {
				run(max);
			}

			// timed
			Instant start = Instant.now();
			for (int i = 0; i < TIMED_ROUNDS; i++) {
				run(max);
			}
			Instant end = Instant.now();

			// averaged result
			Duration elapsed = Duration.between(start, end);
			return (double) elapsed.toMillis() / TIMED_ROUNDS;
		}
	}

	private static class SingleBenchmarker extends Benchmarker {

		@Override
		public void run(int max) {
			PrimeFinder.trialDivision(0, max);
		}

	}

	private static class MultiBenchmarker extends Benchmarker {

		private final int threads;

		public MultiBenchmarker(int threads) {
			this.threads = threads;
		}

		@Override
		public void run(int max) {
			PrimeFinder.findPrimes(1, max, threads);
		}
	}

}
