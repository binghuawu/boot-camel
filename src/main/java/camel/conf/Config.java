package camel.conf;

import org.apache.camel.spring.CamelContextFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import camel.route.Route1;

@Configuration
public class Config {

	@Bean
	CamelContextFactoryBean camelContext() {
		CamelContextFactoryBean ccf = new CamelContextFactoryBean();
		ccf.setId("cc1");
		ccf.setPackages(new String[] { Route1.class.getPackage().getName() });
		return ccf;
	}
}