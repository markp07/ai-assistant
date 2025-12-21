package nl.markpost.aiassistant.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.models.MessageDTO;
import nl.markpost.aiassistant.models.entity.ChatMessage;
import nl.markpost.aiassistant.models.entity.ChatSession;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ChatSessionMapperTest {

  private final ChatSessionMapper chatSessionMapper = Mappers.getMapper(ChatSessionMapper.class);

  @Test
  void toDTOWithoutMessages_shouldMapChatSessionWithoutMessages() {
    ChatSession session = new ChatSession();
    session.setId("session-123");
    session.setUserId("user-123");
    session.setTitle("Test Chat");
    session.setCreatedAt(LocalDateTime.now());
    session.setUpdatedAt(LocalDateTime.now());
    session.setMessages(new ArrayList<>());

    ChatSessionDTO result = chatSessionMapper.toDTOWithoutMessages(session);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("session-123");
    assertThat(result.getTitle()).isEqualTo("Test Chat");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getMessages()).isNull();
  }

  @Test
  void toDTOWithMessages_shouldMapChatSessionWithMessages() {
    ChatSession session = new ChatSession();
    session.setId("session-123");
    session.setUserId("user-123");
    session.setTitle("Test Chat");
    session.setCreatedAt(LocalDateTime.now());
    session.setUpdatedAt(LocalDateTime.now());

    ChatMessage message1 = new ChatMessage();
    message1.setId("msg-1");
    message1.setChatSession(session);
    message1.setRole("user");
    message1.setContent("Hello");
    message1.setTimestamp(LocalDateTime.now());

    ChatMessage message2 = new ChatMessage();
    message2.setId("msg-2");
    message2.setChatSession(session);
    message2.setRole("assistant");
    message2.setContent("Hi there!");
    message2.setTimestamp(LocalDateTime.now());

    List<ChatMessage> messages = List.of(message1, message2);
    session.setMessages(messages);

    ChatSessionDTO result = chatSessionMapper.toDTOWithMessages(session);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("session-123");
    assertThat(result.getTitle()).isEqualTo("Test Chat");
    assertThat(result.getMessages()).isNotNull();
    assertThat(result.getMessages()).hasSize(2);
    assertThat(result.getMessages().get(0).getId()).isEqualTo("msg-1");
    assertThat(result.getMessages().get(1).getId()).isEqualTo("msg-2");
  }

  @Test
  void toMessageDTO_shouldMapChatMessage() {
    ChatSession session = new ChatSession();
    session.setId("session-123");

    ChatMessage message = new ChatMessage();
    message.setId("msg-123");
    message.setChatSession(session);
    message.setRole("user");
    message.setContent("Hello, world!");
    message.setTimestamp(LocalDateTime.now());

    MessageDTO result = chatSessionMapper.toMessageDTO(message);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("msg-123");
    assertThat(result.getRole()).isEqualTo("user");
    assertThat(result.getContent()).isEqualTo("Hello, world!");
    assertThat(result.getTimestamp()).isNotNull();
  }

  @Test
  void toChatSession_shouldCreateChatSessionFromUserIdAndTitle() {
    String userId = "user-123";
    String title = "New Chat";

    ChatSession result = chatSessionMapper.toChatSession(userId, title);

    assertThat(result).isNotNull();
    assertThat(result.getUserId()).isEqualTo("user-123");
    assertThat(result.getTitle()).isEqualTo("New Chat");
    assertThat(result.getId()).isNull(); // Should be ignored
    // Messages field may be initialized as empty list by @Builder.Default
    assertThat(result.getMessages()).isNullOrEmpty();
    assertThat(result.getCreatedAt()).isNull(); // Should be ignored
    assertThat(result.getUpdatedAt()).isNull(); // Should be ignored
  }

  @Test
  void toChatMessage_shouldCreateChatMessageFromSessionRoleAndContent() {
    ChatSession session = new ChatSession();
    session.setId("session-123");
    session.setUserId("user-123");
    String role = "user";
    String content = "Hello, AI!";

    ChatMessage result = chatSessionMapper.toChatMessage(session, role, content);

    assertThat(result).isNotNull();
    assertThat(result.getChatSession()).isEqualTo(session);
    assertThat(result.getRole()).isEqualTo("user");
    assertThat(result.getContent()).isEqualTo("Hello, AI!");
    assertThat(result.getId()).isNull(); // Should be ignored
    assertThat(result.getTimestamp()).isNull(); // Should be ignored
  }

  @Test
  void toChatMessage_shouldHandleAssistantRole() {
    ChatSession session = new ChatSession();
    session.setId("session-123");
    String role = "assistant";
    String content = "Response from AI";

    ChatMessage result = chatSessionMapper.toChatMessage(session, role, content);

    assertThat(result).isNotNull();
    assertThat(result.getRole()).isEqualTo("assistant");
    assertThat(result.getContent()).isEqualTo("Response from AI");
  }
}
