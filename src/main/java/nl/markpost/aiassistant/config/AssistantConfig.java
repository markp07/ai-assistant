package nl.markpost.aiassistant.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import nl.markpost.aiassistant.service.Assistant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig {

  @Bean
  public Assistant createAssistant(
      OpenAiChatModel model, OpenAiStreamingChatModel streamingModel, ChatMemory memory) {
    return AiServices.builder(Assistant.class)
        .chatModel(model)
        .streamingChatModel(streamingModel)
        .chatMemory(memory)
        .build();
  }
}
