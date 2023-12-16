package be.vives.ti.barnespizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.logging.Logger;

@SpringBootApplication
public class BarnesPizzaApplication {

	private static final Logger logger = Logger.getLogger(BarnesPizzaApplication.class.getName());
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BarnesPizzaApplication.class);
		Environment env = app.run(args).getEnvironment();
		logStartupInfo(env);
	}

	private static void logStartupInfo(Environment env) {
		logger.info("Application" + env.getProperty("spring.application.name") + " is running on port: " + env.getProperty("server.port"));
		logger.info("Application" + env.getProperty("spring.application.name") + " is running in profile: " + env.getProperty("spring.profiles.active"));
	}
}
