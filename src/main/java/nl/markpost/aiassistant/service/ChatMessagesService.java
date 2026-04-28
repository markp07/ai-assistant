package nl.markpost.aiassistant.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.markpost.aiassistant.exception.BadRequestException;
import nl.markpost.aiassistant.mapper.ChatSessionMapper;
import nl.markpost.aiassistant.models.MessageDTO;
import nl.markpost.aiassistant.models.entity.ChatMessage;
import nl.markpost.aiassistant.models.entity.ChatSession;
import nl.markpost.aiassistant.repository.ChatMessageRepository;
import nl.markpost.aiassistant.repository.ChatSessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service for managing chat sessions and messages. */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessagesService {

  private final ChatSessionRepository chatSessionRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final AiProviderService aiProviderService;
  private final ChatMemory chatMemory;
  private final ChatSessionMapper chatSessionMapper;

  /**
   * Sends a message in the specified chat session and gets a response from the assistant.
   *
   * @param sessionId The ID of the chat session.
   * @param userId The ID of the user.
   * @param messageContent The content of the user's message.
   * @param provider The AI provider to use ("openai" or "ollama"). Defaults to "openai".
   * @param model The model name (required when provider is "ollama").
   * @return The assistant's response as a MessageDTO.
   */
  @Transactional
  public MessageDTO sendMessage(
      String sessionId, String userId, String messageContent, String provider, String model) {
    ChatSession session = getSessionEntity(sessionId, userId);

    ChatMessage userMessage = chatSessionMapper.toChatMessage(session, "user", messageContent);
    chatMessageRepository.save(userMessage);

    chatMemory.clear();
    List<ChatMessage> recentMessages =
        chatMessageRepository.findLastMessagesBySessionId(sessionId, PageRequest.of(0, 9));
    Collections.reverse(recentMessages);

    for (ChatMessage msg : recentMessages) {
      if ("user".equals(msg.getRole())) {
        chatMemory.add(UserMessage.from(msg.getContent()));
      } else {
        chatMemory.add(AiMessage.from(msg.getContent()));
      }
    }

    String assistantResponse = aiProviderService.chat(provider, model, messageContent, chatMemory);

    ChatMessage assistantMessage =
        chatSessionMapper.toChatMessage(session, "assistant", assistantResponse);
    assistantMessage = chatMessageRepository.save(assistantMessage);

    return chatSessionMapper.toMessageDTO(assistantMessage);
  }

  /**
   * Retrieves the message history for the specified chat session.
   *
   * @param sessionId The ID of the chat session.
   * @param userId The ID of the user.
   * @return A list of MessageDTOs representing the session history.
   */
  @Transactional(readOnly = true)
  public List<MessageDTO> getSessionHistory(String sessionId, String userId) {
    List<ChatMessage> messages =
        chatMessageRepository.findByChatSessionIdOrderByTimestampAsc(sessionId);

    return messages.stream().map(chatSessionMapper::toMessageDTO).collect(Collectors.toList());
  }

  /**
   * Helper method to retrieve a ChatSession entity by ID and user ID.
   *
   * @param sessionId The ID of the chat session.
   * @param userId The ID of the user.
   * @return The ChatSession entity.
   * @throws BadRequestException if the session is not found.
   */
  private ChatSession getSessionEntity(String sessionId, String userId) {
    return chatSessionRepository
        .findByIdAndUserId(sessionId, userId)
        .orElseThrow(() -> new BadRequestException("Session not found"));
  }
}
