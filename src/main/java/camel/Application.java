package camel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {

	public static void main(String... args) {
		new SpringApplicationBuilder(Application.class).web(false).run(args);
		// try {
		// new BufferedReader(new InputStreamReader(System.in)).readLine();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}
