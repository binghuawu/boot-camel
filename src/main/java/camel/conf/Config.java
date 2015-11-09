package camel.conf;

import org.apache.camel.spring.CamelContextFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import camel.route.RepeatRoute;

@Configuration
public class Config {

	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean ccf = new CamelContextFactoryBean();
		ccf.setId("cc1");
		ccf.setPackages(new String[] { RepeatRoute.class.getPackage().getName() });
		return ccf;
	}
}