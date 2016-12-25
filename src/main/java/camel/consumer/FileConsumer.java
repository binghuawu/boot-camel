package camel.consumer;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import camel.RandomSleep;

@Component
public class FileConsumer {
	private final Logger LOG = LoggerFactory.getLogger(FileConsumer.class);

	public void consume(Exchange ex) {
		LOG.debug("Handling file {}:{}",
				ex.getIn().getHeader("CamelFileAbsolutePath"), ex.getIn()
						.getHeader("CamelFileLength"));
		LOG.info("Body: {}", ex.getIn().getBody());
		RandomSleep.sleep(4, 1);
	}
}
