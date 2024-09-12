package nl.markpost.aiassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * The main class of the application.
 *
 * This class is responsible for starting the Spring Boot application.
 *
 * The {@link SpringBootApplication} annotation is used to enable the auto-configuration of the Spring application context.
 * The {@link ServletComponentScan} annotation is used to scan for Servlet components.
 *
 * The main method starts the Spring Boot application.
 *
 * @see SpringBootApplication
 * @see ServletComponentScan
 */
@SpringBootApplication
@ServletComponentScan
public class AiAssistant {

    public static void main(String[] args) {
        SpringApplication.run(AiAssistant.class, args);
    }

}
