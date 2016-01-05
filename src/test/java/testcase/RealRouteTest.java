package testcase;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import camel.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@SpringApplicationConfiguration(classes = { Application.class, TestConfig.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = { "classpath:application-test.properties" })
public class RealRouteTest {

	@Value("${routeA.from.uri}")
	String routeAFrom;

	@Autowired
	CamelContext cc;

	@Test
	public void testFilePoller() throws Exception {
		Endpoint ep = cc.getEndpoint(routeAFrom);
		Assert.assertNotNull(ep);

		ProducerTemplate pt = cc.createProducerTemplate();
		pt.start();
		pt.sendBody(ep, "Hello from test");
	}
}