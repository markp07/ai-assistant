package nl.markpost.aiassistant.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

  @Value("${openai.api-key}")
  private String apiKey;

  @Bean
  public OpenAiChatModel openAiChatModel() {
    return OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(OpenAiChatModelName.GPT_5_MINI)
        .temperature(0.5)
        .build();
  }
}
