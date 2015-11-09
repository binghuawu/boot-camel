package testcase;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
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

	@EndpointInject(uri = "mock:log:logA")
	protected MockEndpoint log;

	@Produce(uri = "mock://timer:timerA")
	protected ProducerTemplate timer;

	@Test
	public void testPositive() throws Exception {
		cc.getRouteDefinitions().get(0)
				.adviceWith(cc, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {
						// mock all endpoints
						mockEndpointsAndSkip();
					}
				});
		log.expectedBodiesReceived("David");

		timer.sendBody("David");

		MockEndpoint.assertIsSatisfied(cc);
	}
}