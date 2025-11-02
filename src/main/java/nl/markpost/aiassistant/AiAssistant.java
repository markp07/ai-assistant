package nl.markpost.aiassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The main class of the application.
 *
 * <p>This class is responsible for starting the Spring Boot application.
 *
 * <p>The {@link SpringBootApplication} annotation is used to enable the auto-configuration of the
 * Spring application context. The {@link ServletComponentScan} annotation is used to scan for
 * Servlet components.
 *
 * <p>The main method starts the Spring Boot application.
 *
 * @see SpringBootApplication
 * @see ServletComponentScan
 */
@SpringBootApplication
@ServletComponentScan
@EnableFeignClients
public class AiAssistant {

  public static void main(String[] args) {
    SpringApplication.run(AiAssistant.class, args);
  }
}
