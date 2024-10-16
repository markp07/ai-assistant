package nl.markpost.aiassistant.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NsTravelInformationClientConfig {

  @Value("${ns.api-key}")
  private String apiKey;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return new RequestInterceptor() {
      @Override
      public void apply(RequestTemplate template) {
        template.header("Ocp-Apim-Subscription-Key", apiKey);
      }
    };
  }
}
