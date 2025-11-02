package nl.markpost.aiassistant.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import nl.markpost.aiassistant.service.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig {

  @Autowired private OpenAiChatModel model;

  @Autowired private ChatMemory memory;

  @Bean
  public Assistant createAssistant() {
    return AiServices.builder(Assistant.class).chatModel(model).chatMemory(memory).build();
  }
}
