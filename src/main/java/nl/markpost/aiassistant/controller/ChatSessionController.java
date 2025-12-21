package nl.markpost.aiassistant.controller;

import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.controller.SessionsApi;
import nl.markpost.aiassistant.api.model.ChatSession;
import nl.markpost.aiassistant.api.model.CreateSessionRequest;
import nl.markpost.aiassistant.api.model.Message;
import nl.markpost.aiassistant.api.model.SendMessageRequest;
import nl.markpost.aiassistant.api.model.UpdateSessionRequest;
import nl.markpost.aiassistant.mapper.SessionApiMapper;
import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.models.MessageDTO;
import nl.markpost.aiassistant.service.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller for managing chat sessions.
 * Implements the generated SessionsApi interface from OpenAPI specification.
 */
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatSessionController implements SessionsApi {

    private final ChatSessionService chatSessionService;
    private final SessionApiMapper sessionApiMapper;

    /**
     * Creates a new chat session for the authenticated user.
     *
     * @param createSessionRequest the request body containing session details
     * @return a ResponseEntity containing the created ChatSession
     */
    @Override
    public ResponseEntity<ChatSession> createSession(CreateSessionRequest createSessionRequest) {
        String userId = getUserId();
        String title = createSessionRequest.getTitle();
        ChatSessionDTO sessionDTO = chatSessionService.createSession(userId, title);
        ChatSession response = sessionApiMapper.toApiModel(sessionDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all chat sessions for the authenticated user.
     *
     * @return a ResponseEntity containing a list of ChatSessions
     */
    @Override
    public ResponseEntity<List<ChatSession>> getUserSessions() {
        String userId = getUserId();
        List<ChatSessionDTO> sessionsDTO = chatSessionService.getUserSessions(userId);
        List<ChatSession> response = sessionApiMapper.toApiModelList(sessionsDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific chat session by its ID for the authenticated user.
     *
     * @param sessionId the ID of the chat session
     * @return a ResponseEntity containing the ChatSession
     */
    @Override
    public ResponseEntity<ChatSession> getSession(String sessionId) {
        String userId = getUserId();
        ChatSessionDTO sessionDTO = chatSessionService.getSession(sessionId, userId);
        ChatSession response = sessionApiMapper.toApiModel(sessionDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the message history for a specific chat session.
     *
     * @param sessionId the ID of the chat session
     * @return a ResponseEntity containing a list of Messages
     */
    @Override
    public ResponseEntity<List<Message>> getSessionHistory(String sessionId) {
        String userId = getUserId();
        List<MessageDTO> historyDTO = chatSessionService.getSessionHistory(sessionId, userId);
        List<Message> response = sessionApiMapper.toApiMessageList(historyDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Sends a message in a chat session.
     *
     * @param sessionId          the ID of the chat session
     * @param sendMessageRequest the request body containing the message
     * @return a ResponseEntity containing the sent Message
     */
    @Override
    public ResponseEntity<Message> sendMessage(String sessionId, SendMessageRequest sendMessageRequest) {
        String userId = getUserId();
        String messageContent = sendMessageRequest.getMessage();
        MessageDTO messageDTO = chatSessionService.sendMessage(sessionId, userId, messageContent);
        Message response = sessionApiMapper.toApiModel(messageDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the title of a chat session.
     *
     * @param sessionId            the ID of the chat session to update
     * @param updateSessionRequest the request body containing the new title
     * @return a ResponseEntity containing the updated ChatSession
     */
    @Override
    public ResponseEntity<ChatSession> updateSession(String sessionId, UpdateSessionRequest updateSessionRequest) {
        String userId = getUserId();
        String newTitle = updateSessionRequest.getTitle();
        ChatSessionDTO sessionDTO = chatSessionService.updateSessionTitle(sessionId, userId, newTitle);
        ChatSession response = sessionApiMapper.toApiModel(sessionDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a chat session by its ID for the authenticated user.
     *
     * @param sessionId the ID of the chat session to delete
     * @return a ResponseEntity with no content
     */
    @Override
    public ResponseEntity<Void> deleteSession(String sessionId) {
        String userId = getUserId();
        chatSessionService.deleteSession(sessionId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to extract user ID from the security context.
     *
     * @return the authenticated user's ID
     */
    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}

