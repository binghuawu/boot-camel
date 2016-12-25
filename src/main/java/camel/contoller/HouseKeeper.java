package camel.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class HouseKeeper {
	@Autowired
	private HazelcastIdempotentRepository repo;

	@Scheduled(fixedDelay = 1000 * 60, initialDelay = 0)
	public void clearIdempotentRepo() {
		// repo.clear();
	}
}
