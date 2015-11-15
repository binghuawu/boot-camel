package testcase;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.BootstrapWith;

import camel.Application;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@WebIntegrationTest
@SpringApplicationConfiguration(classes = { Application.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
// @MockEndpoints
public class RepeatRouteTest extends CamelTestSupport {

	@Autowired
	protected ModelCamelContext cc;

	// @EndpointInject(uri = "mock:log:logA")
	protected MockEndpoint log;

	// @Produce(uri = "mock://timer:timerA")
	protected MockEndpoint timer;

	@Override
	public String isMockEndpoints() {
		// override this method and return the pattern for which endpoints to
		// mock.
		// use * to indicate all
		return "*";
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
			}
		};
	}

	@Test
	public void testPositive() throws Exception {
		log = super.getMockEndpoint("mock:log:logA");
		log.expectedBodiesReceived("David");

		sendBody("log:logA", "David");

		assertMockEndpointsSatisfied();
	}

	@Test
	public void testFilePoller() throws Exception {
		getMockEndpoint("mock:bean:fileConsumer").expectedBodiesReceived("xxx");

		sendBody("bean:fileConsumer", "xxx");

		assertMockEndpointsSatisfied();
	}
}