package camel.contoller;

import org.apache.camel.spi.IdempotentRepository;

/**
 * Idempotent repository which supports clustered mode.
 * 
 * @author binghuawu
 * 
 * @param <T>
 *            type of Key stored in the repository
 */
public interface ClusteredIdempotentRepository<T> extends IdempotentRepository<T> {

}
