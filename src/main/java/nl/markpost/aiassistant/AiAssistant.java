package nl.markpost.aiassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The main class of the AI Assistant application.
 *
 * <p>The {@link SpringBootApplication} annotation enables auto-configuration of the Spring
 * application context, and {@link EnableFeignClients} enables Feign client support.
 *
 * @see SpringBootApplication
 * @see EnableFeignClients
 */
@SpringBootApplication
@EnableFeignClients
public class AiAssistant {

  static void main() {
    SpringApplication.run(AiAssistant.class).getEnvironment();
  }
}
