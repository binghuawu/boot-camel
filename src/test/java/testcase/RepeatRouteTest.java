package testcase;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints("log:*")
public class RepeatRouteTest {

	@Autowired
	protected CamelContext camelContext2;

	protected MockEndpoint mockB;

	@EndpointInject(uri = "mock:c", context = "camelContext2")
	protected MockEndpoint mockC;

	@Produce(uri = "direct:start2", context = "camelContext2")
	protected ProducerTemplate start2;

	@EndpointInject(uri = "mock:log:org.apache.camel.test.junit4.spring", context = "camelContext2")
	protected MockEndpoint mockLog;

	@Test
	public void testPositive() throws Exception {

		mockC.expectedBodiesReceived("David");
		mockLog.expectedBodiesReceived("Hello David");

		start2.sendBody("David");

		MockEndpoint.assertIsSatisfied(camelContext2);
	}
}