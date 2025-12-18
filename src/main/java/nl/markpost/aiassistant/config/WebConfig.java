package nl.markpost.aiassistant.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${cors.allowed-origins}")
  private String allowedOrigins;

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    log.info("Configuring CORS with allowed origins: {}", allowedOrigins);
    registry
        .addMapping("/**")
        .allowedOrigins(allowedOrigins.split(","))
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
  }
}
