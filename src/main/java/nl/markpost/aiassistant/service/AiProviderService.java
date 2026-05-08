package nl.markpost.aiassistant.service;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Service that selects and delegates to the appropriate AI provider (OpenAI or Ollama). */
@Service
@RequiredArgsConstructor
public class AiProviderService {

  private final OpenAiChatModel openAiChatModel;

  @Value("${ollama.base-url}")
  private String ollamaBaseUrl;

  /**
   * Sends a chat message using the specified provider and model.
   *
   * @param provider The AI provider ("openai" or "ollama"). Defaults to "openai" if null.
   * @param model The model name (required when provider is "ollama").
   * @param message The user message to send (already present in chatMemory).
   * @param chatMemory The chat memory containing conversation history including the current message.
   * @return The assistant's response.
   */
  public String chat(String provider, String model, String message, ChatMemory chatMemory) {
    ChatModel chatModel = resolveModel(provider, model);
    List<ChatMessage> messages = chatMemory.messages();
    return chatModel.chat(messages);
  }

  private ChatModel resolveModel(String provider, String model) {
    if ("ollama".equals(provider)) {
      if (model == null || model.isBlank()) {
        throw new BadRequestException(
            "A model name must be specified when using the Ollama provider.");
      }
      return OllamaChatModel.builder().baseUrl(ollamaBaseUrl).modelName(model).build();
    }
    return openAiChatModel;
  }
}
