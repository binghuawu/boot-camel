package camel;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileHandler.class);

	public void handle(Exchange exchange) {

		LOGGER.debug("Handling file {}:{}",
				exchange.getIn().getHeader("CamelFileAbsolutePath"), exchange
						.getIn().getHeader("CamelFileLength"));
		RandomSleep.sleep(10, 3);
	}
}
