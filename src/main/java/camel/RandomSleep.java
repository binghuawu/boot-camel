package camel;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RandomSleep {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RandomSleep.class);

	public static void sleep(int maxSec, int minSec) {
		if (maxSec < minSec || maxSec < 0 || minSec < 0)
			throw new IllegalArgumentException();
		Random r = new Random();
		int next = r.nextInt(maxSec);
		if (next < minSec) {
			next = minSec;
		}
		try {
			LOGGER.info("Sleeping for {} secs", next);
			Thread.sleep(1000 * next);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
