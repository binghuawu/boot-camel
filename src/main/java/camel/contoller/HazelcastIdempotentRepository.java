package camel.contoller;

import java.util.Map;

import org.apache.camel.spi.IdempotentRepository;
import org.apache.camel.support.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;

@Component("clusteredIdempotentRepo")
public class HazelcastIdempotentRepository extends ServiceSupport implements
		IdempotentRepository<String> {
	private static final Logger LOG = LoggerFactory
			.getLogger(HazelcastIdempotentRepository.class);
	@Autowired
	private transient HazelcastInstance hcInstance;
	private transient Map<String, Object> theMap;
	private transient ILock theLock;
	private final int SIZE = 1000;

	private String computeName() {
		return hcInstance.getName() + "-map-idempotentRepo";
	}

	@Override
	public boolean add(String key) {
		try {
			theLock.lock();
			LOG.info("trying to add key: {}, map size:{}", key, theMap.size());
			if (theMap.containsKey(key)) {
				return false;
			} else {
				theMap.put(key, key);
				LOG.info("added key: {}, map size:{}", key, theMap.size());
				return true;
			}
		} finally {
			theLock.unlock();
		}
	}

	@Override
	public boolean contains(String key) {
		try {
			theLock.lock();
			return theMap.containsKey(key);
		} finally {
			theLock.unlock();
		}
	}

	@Override
	public boolean remove(String key) {
		try {
			theLock.lock();
			return theMap.remove(key) != null;
		} finally {
			theLock.unlock();
		}
	}

	@Override
	public boolean confirm(String key) {
		// noop
		return true;
	}

	@Override
	public void clear() {
		LOG.info("{} cleared", theMap.size());
		theMap.clear();
	}

	@Override
	protected void doStart() throws Exception {
		theMap = hcInstance.getMap(computeName());
		theLock = hcInstance.getLock(computeName());
	}

	@Override
	protected void doStop() throws Exception {
		try {
			theMap.clear();
		} catch (Exception ex) {
			// ignore
			LOG.warn("stop with error:{}", ex.getMessage());
		}
	}
}
