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

    @PostMapping
    public ResponseEntity<ChatSessionDTO> createSession(
            @RequestBody CreateSessionRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        ChatSessionDTO session = chatSessionService.createSession(userId, request.getTitle());
        return ResponseEntity.ok(session);
    }

    @GetMapping
    public ResponseEntity<List<ChatSessionDTO>> getUserSessions(Authentication authentication) {
        String userId = authentication.getName();
        List<ChatSessionDTO> sessions = chatSessionService.getUserSessions(userId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ChatSessionDTO> getSession(
            @PathVariable String sessionId,
            Authentication authentication) {
        String userId = authentication.getName();
        ChatSessionDTO session = chatSessionService.getSession(sessionId, userId);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/{sessionId}/history")
    public ResponseEntity<List<MessageDTO>> getSessionHistory(
            @PathVariable String sessionId,
            Authentication authentication) {
        String userId = authentication.getName();
        List<MessageDTO> history = chatSessionService.getSessionHistory(sessionId, userId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable String sessionId,
            @RequestBody SendMessageRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        MessageDTO response = chatSessionService.sendMessage(sessionId, userId, request.getMessage());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<ChatSessionDTO> updateSession(
            @PathVariable String sessionId,
            @RequestBody CreateSessionRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        ChatSessionDTO session = chatSessionService.updateSessionTitle(sessionId, userId, request.getTitle());
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable String sessionId,
            Authentication authentication) {
        String userId = authentication.getName();
        chatSessionService.deleteSession(sessionId, userId);
        return ResponseEntity.noContent().build();
    }
}

