package camel.contoller;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileExclusiveReadLockStrategy;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.camel.spi.IdempotentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A file read lock that uses an {@link org.apache.camel.spi.IdempotentRepository} as the lock strategy. This
 * allows to plugin and use existing idempotent repositories that for example
 * supports clustering. The other read lock strategies that are using marker
 * files or file locks, are not guaranteed to work in clustered setup with
 * various platform and file systems.
 */
@Component("clusteredReadLockStrategy")
public class ClusteredIdempotentRepositoryReadLockStrategy implements GenericFileExclusiveReadLockStrategy<File> {

	private static final transient Logger LOG = LoggerFactory
		.getLogger(ClusteredIdempotentRepositoryReadLockStrategy.class);

	@Autowired
	private ClusteredIdempotentRepository<String> idempotentRepository;
	private GenericFileEndpoint<File> endpoint;

	@Override
	public void prepareOnStartup(GenericFileOperations<File> operations, GenericFileEndpoint<File> endpoint)
		throws Exception {
		this.endpoint = endpoint;
		LOG.info("Using ClusteredIdempotentRepositoryReadLockStrategy: {} on endpoint: {}", idempotentRepository,
			endpoint);
	}

	@Override
	public boolean acquireExclusiveReadLock(GenericFileOperations<File> operations, GenericFile<File> file,
		Exchange exchange) throws Exception {
		// in clustered mode then another node may have processed the file so we
		// must check here again if the file exists
		File path = file.getFile();
		if (!path.exists()) {
			return false;
		}

		// check if we can begin on this file
		String key = asKey(file);
		boolean answer = idempotentRepository.add(key);
		if (!answer) {
			// another node is processing the file so skip
			LOG.info("Cannot acquire read lock. Will skip the file: " + file);
		}
		return answer;
	}

	@Override
	public void releaseExclusiveReadLock(GenericFileOperations<File> operations, GenericFile<File> file,
		Exchange exchange) throws Exception {
		String key = asKey(file);
		if (exchange.isRollbackOnly()) {
			idempotentRepository.remove(key);
		} else {
			idempotentRepository.confirm(key);
		}
	}

	public void setTimeout(long timeout) {
		// noop
	}

	public void setCheckInterval(long checkInterval) {
		// noop
	}

	public void setReadLockLoggingLevel(LoggingLevel readLockLoggingLevel) {
		// noop
	}

	public void setMarkerFiler(boolean markerFile) {
		// noop
	}

	public void setDeleteOrphanLockFiles(boolean deleteOrphanLockFiles) {
		// noop
	}

	/**
	 * The idempotent repository to use as the store for the read locks.
	 */
	public IdempotentRepository<String> getIdempotentRepository() {
		return idempotentRepository;
	}

	/**
	 * The idempotent repository to use as the store for the read locks.
	 */
	public void setIdempotentRepository(ClusteredIdempotentRepository<String> idempotentRepository) {
		this.idempotentRepository = idempotentRepository;
	}

	protected String asKey(GenericFile<File> file) {
		// use absolute file path as default key, but evaluate if an expression
		// key was configured
		String key = file.getAbsoluteFilePath();
		if (endpoint.getIdempotentKey() != null) {
			Exchange dummy = endpoint.createExchange(file);
			key = endpoint.getIdempotentKey().evaluate(dummy, String.class);
		}
		return key;
	}
}
