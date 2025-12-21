package nl.markpost.aiassistant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import nl.markpost.aiassistant.exception.BadRequestException;
import nl.markpost.aiassistant.mapper.ChatSessionMapper;
import nl.markpost.aiassistant.models.MessageDTO;
import nl.markpost.aiassistant.models.entity.ChatMessage;
import nl.markpost.aiassistant.models.entity.ChatSession;
import nl.markpost.aiassistant.repository.ChatMessageRepository;
import nl.markpost.aiassistant.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ChatMessagesServiceTest {

  @Mock private ChatSessionRepository chatSessionRepository;

  @Mock private ChatMessageRepository chatMessageRepository;

  @Mock private Assistant assistant;

  @Mock private ChatMemory chatMemory;

  @Mock private ChatSessionMapper chatSessionMapper;

  @InjectMocks private ChatMessagesService chatMessagesService;

  private static final String USER_ID = "user-123";
  private static final String SESSION_ID = "session-123";
  private static final String MESSAGE_CONTENT = "Hello, AI!";
  private static final String ASSISTANT_RESPONSE = "Hello! How can I help you?";

  @Test
  void sendMessage_shouldSaveUserMessageAndReturnAssistantResponse() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);

    ChatMessage userMessage = new ChatMessage();
    userMessage.setId("user-msg-1");
    userMessage.setChatSession(session);
    userMessage.setRole("user");
    userMessage.setContent(MESSAGE_CONTENT);
    userMessage.setTimestamp(LocalDateTime.now());

    ChatMessage assistantMessage = new ChatMessage();
    assistantMessage.setId("assistant-msg-1");
    assistantMessage.setChatSession(session);
    assistantMessage.setRole("assistant");
    assistantMessage.setContent(ASSISTANT_RESPONSE);
    assistantMessage.setTimestamp(LocalDateTime.now());

    MessageDTO messageDTO =
        MessageDTO.builder()
            .id("assistant-msg-1")
            .role("assistant")
            .content(ASSISTANT_RESPONSE)
            .timestamp(LocalDateTime.now())
            .build();

    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID))
        .thenReturn(Optional.of(session));
    when(chatSessionMapper.toChatMessage(session, "user", MESSAGE_CONTENT)).thenReturn(userMessage);
    when(chatMessageRepository.save(userMessage)).thenReturn(userMessage);
    when(chatMessageRepository.findLastMessagesBySessionId(eq(SESSION_ID), any(PageRequest.class)))
        .thenReturn(List.of());
    when(assistant.chat(MESSAGE_CONTENT)).thenReturn(ASSISTANT_RESPONSE);
    when(chatSessionMapper.toChatMessage(session, "assistant", ASSISTANT_RESPONSE))
        .thenReturn(assistantMessage);
    when(chatMessageRepository.save(assistantMessage)).thenReturn(assistantMessage);
    when(chatSessionMapper.toMessageDTO(assistantMessage)).thenReturn(messageDTO);

    MessageDTO result = chatMessagesService.sendMessage(SESSION_ID, USER_ID, MESSAGE_CONTENT);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("assistant-msg-1");
    assertThat(result.getRole()).isEqualTo("assistant");
    assertThat(result.getContent()).isEqualTo(ASSISTANT_RESPONSE);
    verify(chatMemory).clear();
    verify(chatMessageRepository).save(userMessage);
    verify(chatMessageRepository).save(assistantMessage);
    verify(assistant).chat(MESSAGE_CONTENT);
  }

  @Test
  void sendMessage_shouldLoadRecentMessagesIntoChatMemory() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);

    ChatMessage userMessage = new ChatMessage();
    userMessage.setChatSession(session);
    userMessage.setRole("user");
    userMessage.setContent(MESSAGE_CONTENT);

    ChatMessage previousUserMsg = new ChatMessage();
    previousUserMsg.setRole("user");
    previousUserMsg.setContent("Previous user message");

    ChatMessage previousAiMsg = new ChatMessage();
    previousAiMsg.setRole("assistant");
    previousAiMsg.setContent("Previous AI message");

    // Use ArrayList for mutable list
    List<ChatMessage> recentMessages =
        new java.util.ArrayList<>(List.of(previousUserMsg, previousAiMsg));

    ChatMessage assistantMessage = new ChatMessage();
    assistantMessage.setId("assistant-msg-1");
    assistantMessage.setChatSession(session);
    assistantMessage.setRole("assistant");
    assistantMessage.setContent(ASSISTANT_RESPONSE);

    MessageDTO messageDTO =
        MessageDTO.builder()
            .id("assistant-msg-1")
            .role("assistant")
            .content(ASSISTANT_RESPONSE)
            .build();

    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID))
        .thenReturn(Optional.of(session));
    when(chatSessionMapper.toChatMessage(session, "user", MESSAGE_CONTENT)).thenReturn(userMessage);
    when(chatMessageRepository.save(any(ChatMessage.class)))
        .thenReturn(userMessage)
        .thenReturn(assistantMessage);
    when(chatMessageRepository.findLastMessagesBySessionId(eq(SESSION_ID), any(PageRequest.class)))
        .thenReturn(recentMessages);
    when(assistant.chat(MESSAGE_CONTENT)).thenReturn(ASSISTANT_RESPONSE);
    when(chatSessionMapper.toChatMessage(session, "assistant", ASSISTANT_RESPONSE))
        .thenReturn(assistantMessage);
    when(chatSessionMapper.toMessageDTO(assistantMessage)).thenReturn(messageDTO);

    MessageDTO result = chatMessagesService.sendMessage(SESSION_ID, USER_ID, MESSAGE_CONTENT);

    assertThat(result).isNotNull();
    verify(chatMemory).clear();
    verify(chatMemory).add(any(UserMessage.class));
    verify(chatMemory).add(any(AiMessage.class));
  }

  @Test
  void sendMessage_shouldThrowExceptionWhenSessionNotFound() {
    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> chatMessagesService.sendMessage(SESSION_ID, USER_ID, MESSAGE_CONTENT))
        .isInstanceOf(BadRequestException.class)
        .hasMessage("Session not found");
  }

  @Test
  void getSessionHistory_shouldReturnAllMessagesForSession() {
    ChatMessage message1 = new ChatMessage();
    message1.setId("msg-1");
    message1.setRole("user");
    message1.setContent("Hello");
    message1.setTimestamp(LocalDateTime.now().minusMinutes(2));

    ChatMessage message2 = new ChatMessage();
    message2.setId("msg-2");
    message2.setRole("assistant");
    message2.setContent("Hi there!");
    message2.setTimestamp(LocalDateTime.now().minusMinutes(1));

    List<ChatMessage> messages = List.of(message1, message2);

    MessageDTO dto1 = MessageDTO.builder().id("msg-1").role("user").content("Hello").build();

    MessageDTO dto2 =
        MessageDTO.builder().id("msg-2").role("assistant").content("Hi there!").build();

    when(chatMessageRepository.findByChatSessionIdOrderByTimestampAsc(SESSION_ID))
        .thenReturn(messages);
    when(chatSessionMapper.toMessageDTO(message1)).thenReturn(dto1);
    when(chatSessionMapper.toMessageDTO(message2)).thenReturn(dto2);

    List<MessageDTO> result = chatMessagesService.getSessionHistory(SESSION_ID, USER_ID);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo("msg-1");
    assertThat(result.get(1).getId()).isEqualTo("msg-2");
    verify(chatMessageRepository).findByChatSessionIdOrderByTimestampAsc(SESSION_ID);
  }

  @Test
  void getSessionHistory_shouldReturnEmptyListWhenNoMessages() {
    when(chatMessageRepository.findByChatSessionIdOrderByTimestampAsc(SESSION_ID))
        .thenReturn(List.of());

    List<MessageDTO> result = chatMessagesService.getSessionHistory(SESSION_ID, USER_ID);

    assertThat(result).isEmpty();
    verify(chatMessageRepository).findByChatSessionIdOrderByTimestampAsc(SESSION_ID);
  }
}
