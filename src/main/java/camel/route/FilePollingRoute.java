package camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class FilePollingRoute extends RouteBuilder {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FilePollingRoute.class);

	@Value("${app.in}")
	private String in;

	@Override
	public void configure() throws Exception {
		fromF("file://%s?noop=true", in).to("bean:fileConsumer");
	}
}
