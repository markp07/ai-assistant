package nl.markpost.aiassistant.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.markpost.aiassistant.exception.BadRequestException;
import nl.markpost.aiassistant.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/** Service that selects and delegates to the appropriate AI provider (OpenAI or Ollama). */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiProviderService {

  private final OpenAiChatModel openAiChatModel;

  @Value("${ollama.base-url}")
  private String ollamaBaseUrl;

  @Value("${ollama.log-requests:true}")
  private boolean ollamaLogRequests;

  @Value("${ollama.log-responses:true}")
  private boolean ollamaLogResponses;

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
    List<ChatMessage> messages = chatMemory.messages();
    if ("ollama".equals(provider)) {
      return ollamaChat(model, messages);
    }
    ChatResponse chatResponse = openAiChatModel.chat(messages);
    return chatResponse.aiMessage().text();
  }

  private String ollamaChat(String model, List<ChatMessage> messages) {
    if (model == null || model.isBlank()) {
      throw new BadRequestException(
          "A model name must be specified when using the Ollama provider.");
    }

    if (ollamaLogRequests) {
      log.info(
          "Calling Ollama endpoint {} with model {} (messages={})",
          ollamaBaseUrl + "/api/chat",
          model,
          messages.size());
      log.debug("Ollama request payload messages: {}", messages);
    }

    List<Map<String, String>> ollamaMessages =
        messages.stream().map(this::toOllamaMessage).toList();

    OllamaChatApiResponse response =
        WebClient.builder()
            .baseUrl(ollamaBaseUrl)
            .build()
            .post()
            .uri("/api/chat")
            .bodyValue(Map.of("model", model, "messages", ollamaMessages, "stream", false))
            .retrieve()
            .bodyToMono(OllamaChatApiResponse.class)
            .block();

    if (response == null || response.message() == null) {
      throw new InternalServerErrorException("Empty response from Ollama");
    }

    String result = response.message().content();

    if (ollamaLogResponses) {
      log.info(
          "Received Ollama response from model {} (chars={})",
          model,
          result == null ? 0 : result.length());
      log.debug("Ollama response payload: {}", result);
    }

    return result;
  }

  private Map<String, String> toOllamaMessage(ChatMessage message) {
    String role =
        switch (message.type()) {
          case USER -> "user";
          case AI -> "assistant";
          case SYSTEM -> "system";
          default -> "user";
        };
    String content =
        switch (message) {
          case UserMessage um -> um.singleText();
          case AiMessage am -> am.text();
          case SystemMessage sm -> sm.text();
          default -> "";
        };
    return Map.of("role", role, "content", content != null ? content : "");
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  record OllamaChatApiResponse(@JsonProperty("message") OllamaMessage message) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record OllamaMessage(
      @JsonProperty("role") String role, @JsonProperty("content") String content) {}
}
