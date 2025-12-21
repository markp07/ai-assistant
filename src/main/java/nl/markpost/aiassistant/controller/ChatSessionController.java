package nl.markpost.aiassistant.controller;

import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.models.*;
import nl.markpost.aiassistant.service.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    /**
     * Creates a new chat session for the authenticated user.
     *
     * @param request        the request body containing session details
     * @param authentication the authentication object containing user details
     * @return a ResponseEntity containing the created ChatSessionDTO
     */
    @PostMapping
    public ResponseEntity<ChatSessionDTO> createSession(
            @RequestBody CreateSessionRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        ChatSessionDTO session = chatSessionService.createSession(userId, request.getTitle());
        return ResponseEntity.ok(session);
    }

    /**
     * Retrieves all chat sessions for the authenticated user.
     *
     * @param authentication the authentication object containing user details
     * @return a ResponseEntity containing a list of ChatSessionDTOs
     */
    @GetMapping
    public ResponseEntity<List<ChatSessionDTO>> getUserSessions(Authentication authentication) {
        String userId = authentication.getName();
        List<ChatSessionDTO> sessions = chatSessionService.getUserSessions(userId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Retrieves a specific chat session by its ID for the authenticated user.
     *
     * @param sessionId      the ID of the chat session
     * @param authentication the authentication object containing user details
     * @return a ResponseEntity containing the ChatSessionDTO
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<ChatSessionDTO> getSession(
            @PathVariable String sessionId,
            Authentication authentication) {
        String userId = authentication.getName();
        ChatSessionDTO session = chatSessionService.getSession(sessionId, userId);
        return ResponseEntity.ok(session);
    }

    /**
     * Retrieves the message history for a specific chat session.
     *
     * @param sessionId      the ID of the chat session
     * @param authentication the authentication object containing user details
     * @return a ResponseEntity containing a list of MessageDTOs
     */
    @GetMapping("/{sessionId}/history")
    public ResponseEntity<List<MessageDTO>> getSessionHistory(
            @PathVariable String sessionId,
            Authentication authentication) {
        String userId = authentication.getName();
        List<MessageDTO> history = chatSessionService.getSessionHistory(sessionId, userId);
        return ResponseEntity.ok(history);
    }

    /**
     * Sends a message in a chat session.
     *
     * @param sessionId      the ID of the chat session
     * @param request        the request body containing the message
     * @param authentication the authentication object containing user details
     * @return a ResponseEntity containing the sent MessageDTO
     */
    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable String sessionId,
            @RequestBody SendMessageRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        MessageDTO response = chatSessionService.sendMessage(sessionId, userId, request.getMessage());
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the title of a chat session.
     *
     * @param sessionId      the ID of the chat session to update
     * @param request        the request body containing the new title
     * @param authentication the authentication object containing user details
     * @return a ResponseEntity containing the updated ChatSessionDTO
     */
    @PutMapping("/{sessionId}")
    public ResponseEntity<ChatSessionDTO> updateSession(
            @PathVariable String sessionId,
            @RequestBody CreateSessionRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        ChatSessionDTO session = chatSessionService.updateSessionTitle(sessionId, userId, request.getTitle());
        return ResponseEntity.ok(session);
    }

    /**
     * Deletes a chat session by its ID for the authenticated user.
     *
     * @param sessionId      the ID of the chat session to delete
     * @param authentication the authentication object containing user details
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable String sessionId,
            Authentication authentication) {
        String userId = authentication.getName();
        chatSessionService.deleteSession(sessionId, userId);
        return ResponseEntity.noContent().build();
    }
}

