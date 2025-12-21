package nl.markpost.aiassistant.service;

import dev.langchain4j.memory.ChatMemory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.markpost.aiassistant.exception.BadRequestException;
import nl.markpost.aiassistant.mapper.ChatSessionMapper;
import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.models.entity.ChatSession;
import nl.markpost.aiassistant.repository.ChatMessageRepository;
import nl.markpost.aiassistant.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service for managing chat sessions and messages. */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatSessionService {

  private final ChatSessionRepository chatSessionRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final Assistant assistant;
  private final ChatMemory chatMemory;
  private final ChatSessionMapper chatSessionMapper;

  /**
   * Creates a new chat session for the specified user.
   *
   * @param userId The ID of the user.
   * @param title The title of the chat session.
   * @return The created ChatSessionDTO.
   */
  @Transactional
  public ChatSessionDTO createSession(String userId, String title) {
    String sessionTitle = title != null && !title.isBlank() ? title : "New Chat";
    ChatSession session = chatSessionMapper.toChatSession(userId, sessionTitle);
    session = chatSessionRepository.save(session);
    return chatSessionMapper.toDTOWithoutMessages(session);
  }

  /**
   * Retrieves all chat sessions for the specified user.
   *
   * @param userId The ID of the user.
   * @return A list of ChatSessionDTOs.
   */
  @Transactional(readOnly = true)
  public List<ChatSessionDTO> getUserSessions(String userId) {
    List<ChatSession> sessions = chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    return sessions.stream()
        .map(chatSessionMapper::toDTOWithoutMessages)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a specific chat session by ID for the specified user.
   *
   * @param sessionId The ID of the chat session.
   * @param userId The ID of the user.
   * @return The ChatSessionDTO with messages.
   */
  @Transactional(readOnly = true)
  public ChatSessionDTO getSession(String sessionId, String userId) {
    ChatSession session = getSessionEntity(sessionId, userId);
    return chatSessionMapper.toDTOWithMessages(session);
  }

  /**
   * Updates the title of the specified chat session.
   *
   * @param sessionId The ID of the chat session.
   * @param userId The ID of the user.
   * @param newTitle The new title for the chat session.
   * @return The updated ChatSessionDTO without messages.
   */
  @Transactional
  public ChatSessionDTO updateSessionTitle(String sessionId, String userId, String newTitle) {
    ChatSession session = getSessionEntity(sessionId, userId);

    session.setTitle(newTitle != null && !newTitle.isBlank() ? newTitle : session.getTitle());
    session = chatSessionRepository.save(session);

    return chatSessionMapper.toDTOWithoutMessages(session);
  }

  /**
   * Deletes the specified chat session.
   *
   * @param sessionId The ID of the chat session.
   * @param userId The ID of the user.
   */
  @Transactional
  public void deleteSession(String sessionId, String userId) {
    ChatSession session = getSessionEntity(sessionId, userId);
    chatSessionRepository.delete(session);
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
