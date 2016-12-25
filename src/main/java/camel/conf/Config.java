package camel.conf;

import org.apache.camel.spring.CamelContextFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import camel.route.RepeatRoute;

@Configuration
@ImportResource({ "classpath:hazelcast.spring.xml" })
public class Config {

	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean ccf = new CamelContextFactoryBean();
		ccf.setId("cc1");
		ccf.setPackages(new String[] { RepeatRoute.class.getPackage().getName() });
		return ccf;
	}
}