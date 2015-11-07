package camel.contoller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AController {
	private static final Logger logger = LoggerFactory
			.getLogger(AController.class);

	@RequestMapping("/")
	public String index(Locale locale, Model model, HttpServletRequest r) {
		logger.info("Welcome home! The client locale is {}.", locale);

		return "Hello!!";
	}
}
