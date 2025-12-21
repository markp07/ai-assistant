package nl.markpost.aiassistant.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import nl.markpost.aiassistant.api.model.ChatSession;
import nl.markpost.aiassistant.api.model.CreateSessionRequest;
import nl.markpost.aiassistant.api.model.UpdateSessionRequest;
import nl.markpost.aiassistant.mapper.SessionApiMapper;
import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.service.ChatSessionService;
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
class ChatSessionControllerTest {

  @Mock private ChatSessionService chatSessionService;

  @Mock private SessionApiMapper sessionApiMapper;

  @Mock private SecurityContext securityContext;

  @InjectMocks private ChatSessionController chatSessionController;

  private static final String USER_ID = "test-user-id";
  private static final String SESSION_ID = "session-123";
  private static final String TITLE = "Test Chat";

  @BeforeEach
  void setUp() {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(USER_ID, null);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void createSession_shouldReturnCreatedSession() {
    CreateSessionRequest request = new CreateSessionRequest();
    request.setTitle(TITLE);

    ChatSessionDTO sessionDTO =
        ChatSessionDTO.builder()
            .id(SESSION_ID)
            .title(TITLE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    ChatSession chatSession = new ChatSession();
    chatSession.setId(SESSION_ID);
    chatSession.setTitle(TITLE);
    chatSession.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
    chatSession.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

    when(chatSessionService.createSession(USER_ID, TITLE)).thenReturn(sessionDTO);
    when(sessionApiMapper.toApiModel(sessionDTO)).thenReturn(chatSession);

    ResponseEntity<ChatSession> response = chatSessionController.createSession(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(SESSION_ID);
    assertThat(response.getBody().getTitle()).isEqualTo(TITLE);
    verify(chatSessionService).createSession(USER_ID, TITLE);
    verify(sessionApiMapper).toApiModel(sessionDTO);
  }

  @Test
  void getUserSessions_shouldReturnListOfSessions() {
    ChatSessionDTO session1 = ChatSessionDTO.builder().id("session-1").title("Chat 1").build();

    ChatSessionDTO session2 = ChatSessionDTO.builder().id("session-2").title("Chat 2").build();

    List<ChatSessionDTO> sessionsDTO = List.of(session1, session2);

    ChatSession apiSession1 = new ChatSession();
    apiSession1.setId("session-1");
    apiSession1.setTitle("Chat 1");

    ChatSession apiSession2 = new ChatSession();
    apiSession2.setId("session-2");
    apiSession2.setTitle("Chat 2");

    List<ChatSession> apiSessions = List.of(apiSession1, apiSession2);

    when(chatSessionService.getUserSessions(USER_ID)).thenReturn(sessionsDTO);
    when(sessionApiMapper.toApiModelList(sessionsDTO)).thenReturn(apiSessions);

    ResponseEntity<List<ChatSession>> response = chatSessionController.getUserSessions();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).hasSize(2);
    verify(chatSessionService).getUserSessions(USER_ID);
    verify(sessionApiMapper).toApiModelList(sessionsDTO);
  }

  @Test
  void getSession_shouldReturnSessionById() {
    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title(TITLE).build();

    ChatSession chatSession = new ChatSession();
    chatSession.setId(SESSION_ID);
    chatSession.setTitle(TITLE);

    when(chatSessionService.getSession(SESSION_ID, USER_ID)).thenReturn(sessionDTO);
    when(sessionApiMapper.toApiModel(sessionDTO)).thenReturn(chatSession);

    ResponseEntity<ChatSession> response = chatSessionController.getSession(SESSION_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(SESSION_ID);
    verify(chatSessionService).getSession(SESSION_ID, USER_ID);
    verify(sessionApiMapper).toApiModel(sessionDTO);
  }

  @Test
  void updateSession_shouldReturnUpdatedSession() {
    String newTitle = "Updated Chat";
    UpdateSessionRequest request = new UpdateSessionRequest();
    request.setTitle(newTitle);

    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title(newTitle).build();

    ChatSession chatSession = new ChatSession();
    chatSession.setId(SESSION_ID);
    chatSession.setTitle(newTitle);

    when(chatSessionService.updateSessionTitle(SESSION_ID, USER_ID, newTitle))
        .thenReturn(sessionDTO);
    when(sessionApiMapper.toApiModel(sessionDTO)).thenReturn(chatSession);

    ResponseEntity<ChatSession> response = chatSessionController.updateSession(SESSION_ID, request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getTitle()).isEqualTo(newTitle);
    verify(chatSessionService).updateSessionTitle(SESSION_ID, USER_ID, newTitle);
    verify(sessionApiMapper).toApiModel(sessionDTO);
  }

  @Test
  void deleteSession_shouldReturnNoContent() {
    ResponseEntity<Void> response = chatSessionController.deleteSession(SESSION_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(response.getBody()).isNull();
    verify(chatSessionService).deleteSession(SESSION_ID, USER_ID);
  }
}
