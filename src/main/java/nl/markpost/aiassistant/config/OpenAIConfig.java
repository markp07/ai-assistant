package nl.markpost.aiassistant.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

  @Bean
  public OpenAiChatModel openAiChatModel(@Value("${openai.api-key}") String apiKey) {
    return OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(OpenAiChatModelName.GPT_5_NANO)
        .build();
  }
}
