package camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class FilePollingRoute extends RouteBuilder {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FilePollingRoute.class);

	@Value("${routeA.from.uri}")
	private String from;
	@Value("${routeA.to.uri}")
	private String to;

	@Override
	public void configure() throws Exception {
		from(from).to(to);
	}
}
