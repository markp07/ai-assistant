package nl.markpost.aiassistant.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import nl.markpost.aiassistant.api.model.ChatSession;
import nl.markpost.aiassistant.api.model.Message;
import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.models.MessageDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SessionApiMapperTest {

  private final SessionApiMapper sessionApiMapper = Mappers.getMapper(SessionApiMapper.class);

  @Test
  void toApiModel_shouldMapChatSessionDTO() {
    LocalDateTime now = LocalDateTime.now();
    ChatSessionDTO dto =
        ChatSessionDTO.builder()
            .id("session-123")
            .title("Test Chat")
            .createdAt(now)
            .updatedAt(now)
            .build();

    ChatSession result = sessionApiMapper.toApiModel(dto);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("session-123");
    assertThat(result.getTitle()).isEqualTo("Test Chat");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNotNull();
  }

  @Test
  void toApiModel_shouldHandleNullTimestamps() {
    ChatSessionDTO dto =
        ChatSessionDTO.builder()
            .id("session-123")
            .title("Test Chat")
            .createdAt(null)
            .updatedAt(null)
            .build();

    ChatSession result = sessionApiMapper.toApiModel(dto);

    assertThat(result).isNotNull();
    assertThat(result.getCreatedAt()).isNull();
    assertThat(result.getUpdatedAt()).isNull();
  }

  @Test
  void toApiModelList_shouldMapListOfChatSessionDTOs() {
    LocalDateTime now = LocalDateTime.now();
    ChatSessionDTO dto1 =
        ChatSessionDTO.builder()
            .id("session-1")
            .title("Chat 1")
            .createdAt(now)
            .updatedAt(now)
            .build();

    ChatSessionDTO dto2 =
        ChatSessionDTO.builder()
            .id("session-2")
            .title("Chat 2")
            .createdAt(now)
            .updatedAt(now)
            .build();

    List<ChatSessionDTO> dtos = List.of(dto1, dto2);

    List<ChatSession> result = sessionApiMapper.toApiModelList(dtos);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo("session-1");
    assertThat(result.get(1).getId()).isEqualTo("session-2");
  }

  @Test
  void toApiModel_shouldMapMessageDTO() {
    LocalDateTime now = LocalDateTime.now();
    MessageDTO dto =
        MessageDTO.builder()
            .id("msg-123")
            .role("user")
            .content("Hello, world!")
            .timestamp(now)
            .build();

    Message result = sessionApiMapper.toApiModel(dto);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("msg-123");
    assertThat(result.getRole()).isEqualTo(Message.RoleEnum.USER);
    assertThat(result.getContent()).isEqualTo("Hello, world!");
    assertThat(result.getTimestamp()).isNotNull();
  }

  @Test
  void toApiModel_shouldMapAssistantRole() {
    MessageDTO dto =
        MessageDTO.builder()
            .id("msg-123")
            .role("assistant")
            .content("Hello!")
            .timestamp(LocalDateTime.now())
            .build();

    Message result = sessionApiMapper.toApiModel(dto);

    assertThat(result).isNotNull();
    assertThat(result.getRole()).isEqualTo(Message.RoleEnum.ASSISTANT);
  }

  @Test
  void toApiModel_shouldHandleUppercaseRole() {
    MessageDTO dto =
        MessageDTO.builder()
            .id("msg-123")
            .role("USER")
            .content("Hello!")
            .timestamp(LocalDateTime.now())
            .build();

    Message result = sessionApiMapper.toApiModel(dto);

    assertThat(result).isNotNull();
    assertThat(result.getRole()).isEqualTo(Message.RoleEnum.USER);
  }

  @Test
  void toApiModel_shouldHandleMixedCaseRole() {
    MessageDTO dto =
        MessageDTO.builder()
            .id("msg-123")
            .role("AssiStant")
            .content("Hello!")
            .timestamp(LocalDateTime.now())
            .build();

    Message result = sessionApiMapper.toApiModel(dto);

    assertThat(result).isNotNull();
    assertThat(result.getRole()).isEqualTo(Message.RoleEnum.ASSISTANT);
  }

  @Test
  void toApiMessageList_shouldMapListOfMessageDTOs() {
    LocalDateTime now = LocalDateTime.now();
    MessageDTO dto1 =
        MessageDTO.builder().id("msg-1").role("user").content("Hello").timestamp(now).build();

    MessageDTO dto2 =
        MessageDTO.builder()
            .id("msg-2")
            .role("assistant")
            .content("Hi there!")
            .timestamp(now)
            .build();

    List<MessageDTO> dtos = List.of(dto1, dto2);

    List<Message> result = sessionApiMapper.toApiMessageList(dtos);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo("msg-1");
    assertThat(result.get(0).getRole()).isEqualTo(Message.RoleEnum.USER);
    assertThat(result.get(1).getId()).isEqualTo("msg-2");
    assertThat(result.get(1).getRole()).isEqualTo(Message.RoleEnum.ASSISTANT);
  }

  @Test
  void toOffsetDateTime_shouldConvertLocalDateTimeToOffsetDateTime() {
    LocalDateTime localDateTime = LocalDateTime.of(2025, 12, 21, 10, 30, 0);

    OffsetDateTime result = sessionApiMapper.toOffsetDateTime(localDateTime);

    assertThat(result).isNotNull();
    assertThat(result.getYear()).isEqualTo(2025);
    assertThat(result.getMonthValue()).isEqualTo(12);
    assertThat(result.getDayOfMonth()).isEqualTo(21);
    assertThat(result.getHour()).isEqualTo(10);
    assertThat(result.getMinute()).isEqualTo(30);
    assertThat(result.getOffset()).isEqualTo(ZoneOffset.UTC);
  }

  @Test
  void toOffsetDateTime_shouldHandleNull() {
    OffsetDateTime result = sessionApiMapper.toOffsetDateTime(null);

    assertThat(result).isNull();
  }

  @Test
  void mapRole_shouldHandleNullRole() {
    Message.RoleEnum result = sessionApiMapper.mapRole(null);

    assertThat(result).isNull();
  }
}
