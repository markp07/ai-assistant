package nl.markpost.aiassistant.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.markpost.aiassistant.models.*;
import nl.markpost.aiassistant.models.entity.ChatSession;
import nl.markpost.aiassistant.repository.ChatMessageRepository;
import nl.markpost.aiassistant.repository.ChatSessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final Assistant assistant;
    private final ChatMemory chatMemory;

    @Transactional
    public ChatSessionDTO createSession(String userId, String title) {
        ChatSession session = ChatSession.builder()
                .userId(userId)
                .title(title != null && !title.isBlank() ? title : "New Chat")
                .build();

        session = chatSessionRepository.save(session);
        return mapToDTO(session, false);
    }

    @Transactional(readOnly = true)
    public List<ChatSessionDTO> getUserSessions(String userId) {
        List<ChatSession> sessions = chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        return sessions.stream()
                .map(session -> mapToDTO(session, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatSessionDTO getSession(String sessionId, String userId) {
        ChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return mapToDTO(session, true);
    }

    @Transactional
    public MessageDTO sendMessage(String sessionId, String userId, String messageContent) {
        ChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Save user message
        nl.markpost.aiassistant.models.entity.ChatMessage userMessage =
            nl.markpost.aiassistant.models.entity.ChatMessage.builder()
                .chatSession(session)
                .role("user")
                .content(messageContent)
                .build();
        userMessage = chatMessageRepository.save(userMessage);

        // Clear current memory
        chatMemory.clear();

        // Get last 10 messages for context
        List<nl.markpost.aiassistant.models.entity.ChatMessage> recentMessages =
            chatMessageRepository.findLastMessagesBySessionId(sessionId, PageRequest.of(0, 9));

        // Reverse to get chronological order (excluding current message)
        Collections.reverse(recentMessages);

        // Add context messages to memory
        for (nl.markpost.aiassistant.models.entity.ChatMessage msg : recentMessages) {
            if ("user".equals(msg.getRole())) {
                chatMemory.add(UserMessage.from(msg.getContent()));
            } else {
                chatMemory.add(AiMessage.from(msg.getContent()));
            }
        }

        // Get response from OpenAI using Assistant
        String assistantResponse = assistant.chat(messageContent);

        // Save assistant message
        nl.markpost.aiassistant.models.entity.ChatMessage assistantMessage =
            nl.markpost.aiassistant.models.entity.ChatMessage.builder()
                .chatSession(session)
                .role("assistant")
                .content(assistantResponse)
                .build();
        assistantMessage = chatMessageRepository.save(assistantMessage);

        return mapMessageToDTO(assistantMessage);
    }

    @Transactional
    public ChatSessionDTO updateSessionTitle(String sessionId, String userId, String newTitle) {
        ChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setTitle(newTitle != null && !newTitle.isBlank() ? newTitle : session.getTitle());
        session = chatSessionRepository.save(session);

        return mapToDTO(session, false);
    }

    @Transactional
    public void deleteSession(String sessionId, String userId) {
        ChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        chatSessionRepository.delete(session);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getSessionHistory(String sessionId, String userId) {
        ChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<nl.markpost.aiassistant.models.entity.ChatMessage> messages =
            chatMessageRepository.findByChatSessionIdOrderByTimestampAsc(sessionId);

        return messages.stream()
                .map(this::mapMessageToDTO)
                .collect(Collectors.toList());
    }

    private ChatSessionDTO mapToDTO(ChatSession session, boolean includeMessages) {
        ChatSessionDTO dto = ChatSessionDTO.builder()
                .id(session.getId())
                .title(session.getTitle())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();

        if (includeMessages) {
            List<MessageDTO> messages = session.getMessages().stream()
                    .map(this::mapMessageToDTO)
                    .collect(Collectors.toList());
            dto.setMessages(messages);
        }

        return dto;
    }

    private MessageDTO mapMessageToDTO(nl.markpost.aiassistant.models.entity.ChatMessage message) {
        return MessageDTO.builder()
                .id(message.getId())
                .role(message.getRole())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}

