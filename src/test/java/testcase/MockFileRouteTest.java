package testcase;

import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.TestPropertySource;

import camel.Application;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@WebIntegrationTest
@SpringApplicationConfiguration(classes = { Application.class, TestConfig.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = { "classpath:application-test.properties" })
public class MockFileRouteTest extends CamelTestSupport {

	@Autowired
	protected ModelCamelContext cc;

	@Value("${routeA.from.uri}")
	String routeAFrom;

	@Value("${routeA.to.uri}")
	String routeATo;

	// @Override
	// public String isMockEndpoints() {
	// // override this method and return the pattern for which endpoints to
	// // mock.
	// // use * to indicate all
	// return "*";
	// }

	@Override
	public boolean isUseAdviceWith() {
		return true;
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// from(routeAFrom).to("mock:" + routeATo);
			}
		};
	}

	@Test
	public void testFilePollerB() throws Exception {
		String expected = "xxx";
		final MockEndpoint mockEndpoint = getMockEndpoint("mock:adv");
		cc.getRouteDefinitions().get(0).adviceWith(cc, new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// intercept sending to mock:foo and do something else
				interceptSendToEndpoint(routeATo).to(mockEndpoint);
			}
		});

		mockEndpoint.expectedBodiesReceived(expected);

		Endpoint ep = cc.getEndpoint(routeAFrom);
		Assert.assertNotNull(ep);
		ProducerTemplate pt = cc.createProducerTemplate();
		pt.start();
		pt.sendBody(ep, expected);

		assertMockEndpointsSatisfied();
	}
}