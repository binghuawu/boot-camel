package camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

public class RepeatRoute extends RouteBuilder implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RepeatRoute.class);

	@Value("${app.in}")
	private String in;

	@Override
	public void configure() throws Exception {
		// defaultErrorHandler().setDeadLetterUri("jms-ext:queue:jms/queueDead");
		// errorHandler(defaultErrorHandler().useOriginalMessage().retryWhile(
		// method(RetryOnException.class)));
		//
		// onException(NetworkGateClosedException.class).continued(true);
		// onException(Exception.class).toF("jms-ext:queue:jms/queueDead", "");

		// from("timer:timer1?fixedRate=true&period=100&repeatCount=10")
		// .to("http://www.baidu.com")
		// .log(LoggingLevel.DEBUG,
		// "Response: ${headers.HTTP_RESPONSE_CODE}, ${header.CONTENT_TYPE}");
		// from("timer://timerA?delay=2000").to("log:logA");

		// from("file:///tmp/in?noop=true").to("log:logA");
		// .to("jms-ext:queue:jms/queue02");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}
}
