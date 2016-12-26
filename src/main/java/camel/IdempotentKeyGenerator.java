package camel;

import java.io.File;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component("idempotentKeyGenerator")
public class IdempotentKeyGenerator {

	public String generate(Exchange exchange) {
		String path = (String) exchange.getIn().getHeader("CamelFileAbsolutePath");
		File parent = new File(path).getParentFile();
		if(parent.exists()) {
			return path + "_" + parent.lastModified();
		}
		return path;
	}
}
