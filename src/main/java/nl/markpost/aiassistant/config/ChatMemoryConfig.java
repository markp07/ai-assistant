package nl.markpost.aiassistant.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import nl.markpost.aiassistant.service.CustomChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

  @Bean
  public ChatMemory chatMemory() {
    return MessageWindowChatMemory.builder()
        .maxMessages(10)
        .chatMemoryStore(new CustomChatMemoryStore())
        .build();
  }
}
