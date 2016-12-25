package camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

public class FilePollingRoute extends RouteBuilder implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FilePollingRoute.class);

	@Value("${routeA.from.uri}")
	private String from;
	@Value("${routeA.to.uri}")
	private String to;
	@Value("${app.out}")
	private String appOut;

	@Override
	public void configure() throws Exception {
		from(from).to(to).toF("file://%s", appOut);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// we empty the out folder during startup
		LOGGER.info("Trying to empty {}...", appOut);
	}
}
