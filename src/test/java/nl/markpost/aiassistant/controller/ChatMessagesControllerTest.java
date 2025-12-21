package nl.markpost.aiassistant.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import nl.markpost.aiassistant.api.model.Message;
import nl.markpost.aiassistant.api.model.SendMessageRequest;
import nl.markpost.aiassistant.mapper.SessionApiMapper;
import nl.markpost.aiassistant.models.MessageDTO;
import nl.markpost.aiassistant.service.ChatMessagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ChatMessagesControllerTest {

  @Mock private ChatMessagesService chatMessagesService;

  @Mock private SessionApiMapper sessionApiMapper;

  @Mock private SecurityContext securityContext;

  @InjectMocks private ChatMessagesController chatMessagesController;

  private static final String USER_ID = "test-user-id";
  private static final String SESSION_ID = "session-123";
  private static final String MESSAGE_CONTENT = "Hello, AI!";

  @BeforeEach
  void setUp() {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(USER_ID, null);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void getSessionHistory_shouldReturnMessageList() {
    MessageDTO message1 =
        MessageDTO.builder()
            .id("msg-1")
            .role("user")
            .content("Hello")
            .timestamp(LocalDateTime.now())
            .build();

    MessageDTO message2 =
        MessageDTO.builder()
            .id("msg-2")
            .role("assistant")
            .content("Hi there!")
            .timestamp(LocalDateTime.now())
            .build();

    List<MessageDTO> messagesDTO = List.of(message1, message2);

    Message apiMessage1 =
        Message.builder()
            .id("msg-1")
            .role(Message.RoleEnum.USER)
            .content("Hello")
            .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
            .build();

    Message apiMessage2 =
        Message.builder()
            .id("msg-2")
            .role(Message.RoleEnum.ASSISTANT)
            .content("Hi there!")
            .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
            .build();

    List<Message> apiMessages = List.of(apiMessage1, apiMessage2);

    when(chatMessagesService.getSessionHistory(SESSION_ID, USER_ID)).thenReturn(messagesDTO);
    when(sessionApiMapper.toApiMessageList(messagesDTO)).thenReturn(apiMessages);

    ResponseEntity<List<Message>> response = chatMessagesController.getSessionHistory(SESSION_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).hasSize(2);
    verify(chatMessagesService).getSessionHistory(SESSION_ID, USER_ID);
    verify(sessionApiMapper).toApiMessageList(messagesDTO);
  }

  @Test
  void sendMessage_shouldReturnAssistantResponse() {
    SendMessageRequest request = new SendMessageRequest();
    request.setMessage(MESSAGE_CONTENT);

    MessageDTO responseDTO =
        MessageDTO.builder()
            .id("msg-123")
            .role("assistant")
            .content("Response from AI")
            .timestamp(LocalDateTime.now())
            .build();

    Message apiMessage =
        Message.builder()
            .id("msg-123")
            .role(Message.RoleEnum.ASSISTANT)
            .content("Response from AI")
            .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
            .build();

    when(chatMessagesService.sendMessage(SESSION_ID, USER_ID, MESSAGE_CONTENT))
        .thenReturn(responseDTO);
    when(sessionApiMapper.toApiModel(responseDTO)).thenReturn(apiMessage);

    ResponseEntity<Message> response = chatMessagesController.sendMessage(SESSION_ID, request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo("msg-123");
    assertThat(response.getBody().getRole()).isEqualTo(Message.RoleEnum.ASSISTANT);
    assertThat(response.getBody().getContent()).isEqualTo("Response from AI");
    verify(chatMessagesService).sendMessage(SESSION_ID, USER_ID, MESSAGE_CONTENT);
    verify(sessionApiMapper).toApiModel(responseDTO);
  }
}
