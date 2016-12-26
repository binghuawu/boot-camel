package camel.contoller;

import java.util.Set;

import org.apache.camel.support.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;

@Component("clusteredIdempotentRepo")
public class HazelcastRepository extends ServiceSupport implements ClusteredIdempotentRepository<String> {

	private final static Logger LOG = LoggerFactory.getLogger(HazelcastRepository.class);
	@Autowired
	private transient HazelcastInstance instance;
	private String group;

	public HazelcastRepository() {
		LOG.debug("{} instantiated", getClass());
	}

	@Override
	public boolean add(String key) {
		try {
			instance.getLock(getGroup()).lock();
			Set<String> set = instance.getSet(getGroup());
			if (set.contains(key)) {
				LOG.debug("{} already marked", key);
				return false;
			}
			return set.add(key);
		} finally {
			instance.getLock(getGroup()).unlock();
		}
	}

	@Override
	public boolean contains(String key) {
		try {
			instance.getLock(getGroup()).lock();
			Set<String> set = instance.getSet(getGroup());
			return set.contains(key);
		} finally {
			instance.getLock(getGroup()).unlock();
		}
	}

	@Override
	public boolean remove(String key) {
		try {
			instance.getLock(getGroup()).lock();
			Set<String> set = instance.getSet(getGroup());
			return set.remove(key);
		} finally {
			instance.getLock(getGroup()).unlock();
		}
	}

	@Override
	public boolean confirm(String key) {
		return true;
	}

	@Override
	protected void doStart() throws Exception {
		LOG.debug("{} started", this);
	}

	@Override
	protected void doStop() throws Exception {
	}

	public String getGroup() {
		return group == null ? "idempotent_key" : group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
