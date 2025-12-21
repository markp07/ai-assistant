package nl.markpost.aiassistant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import nl.markpost.aiassistant.exception.BadRequestException;
import nl.markpost.aiassistant.mapper.ChatSessionMapper;
import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.models.entity.ChatSession;
import nl.markpost.aiassistant.repository.ChatMessageRepository;
import nl.markpost.aiassistant.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatSessionServiceTest {

  @Mock private ChatSessionRepository chatSessionRepository;

  @Mock private ChatMessageRepository chatMessageRepository;

  @Mock private Assistant assistant;

  @Mock private dev.langchain4j.memory.ChatMemory chatMemory;

  @Mock private ChatSessionMapper chatSessionMapper;

  @InjectMocks private ChatSessionService chatSessionService;

  private static final String USER_ID = "user-123";
  private static final String SESSION_ID = "session-123";
  private static final String TITLE = "Test Chat";

  @Test
  void createSession_shouldCreateNewSession() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);
    session.setTitle(TITLE);

    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title(TITLE).build();

    when(chatSessionMapper.toChatSession(USER_ID, TITLE)).thenReturn(session);
    when(chatSessionRepository.save(session)).thenReturn(session);
    when(chatSessionMapper.toDTOWithoutMessages(session)).thenReturn(sessionDTO);

    ChatSessionDTO result = chatSessionService.createSession(USER_ID, TITLE);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(SESSION_ID);
    assertThat(result.getTitle()).isEqualTo(TITLE);
    verify(chatSessionMapper).toChatSession(USER_ID, TITLE);
    verify(chatSessionRepository).save(session);
    verify(chatSessionMapper).toDTOWithoutMessages(session);
  }

  @Test
  void createSession_shouldUseDefaultTitleWhenTitleIsNull() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);
    session.setTitle("New Chat");

    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title("New Chat").build();

    when(chatSessionMapper.toChatSession(USER_ID, "New Chat")).thenReturn(session);
    when(chatSessionRepository.save(session)).thenReturn(session);
    when(chatSessionMapper.toDTOWithoutMessages(session)).thenReturn(sessionDTO);

    ChatSessionDTO result = chatSessionService.createSession(USER_ID, null);

    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("New Chat");
    verify(chatSessionMapper).toChatSession(USER_ID, "New Chat");
  }

  @Test
  void createSession_shouldUseDefaultTitleWhenTitleIsBlank() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);
    session.setTitle("New Chat");

    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title("New Chat").build();

    when(chatSessionMapper.toChatSession(USER_ID, "New Chat")).thenReturn(session);
    when(chatSessionRepository.save(session)).thenReturn(session);
    when(chatSessionMapper.toDTOWithoutMessages(session)).thenReturn(sessionDTO);

    ChatSessionDTO result = chatSessionService.createSession(USER_ID, "   ");

    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("New Chat");
  }

  @Test
  void getUserSessions_shouldReturnAllUserSessions() {
    ChatSession session1 = new ChatSession();
    session1.setId("session-1");
    session1.setUserId(USER_ID);
    session1.setTitle("Chat 1");

    ChatSession session2 = new ChatSession();
    session2.setId("session-2");
    session2.setUserId(USER_ID);
    session2.setTitle("Chat 2");

    List<ChatSession> sessions = List.of(session1, session2);

    ChatSessionDTO dto1 = ChatSessionDTO.builder().id("session-1").title("Chat 1").build();
    ChatSessionDTO dto2 = ChatSessionDTO.builder().id("session-2").title("Chat 2").build();

    when(chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(USER_ID)).thenReturn(sessions);
    when(chatSessionMapper.toDTOWithoutMessages(session1)).thenReturn(dto1);
    when(chatSessionMapper.toDTOWithoutMessages(session2)).thenReturn(dto2);

    List<ChatSessionDTO> result = chatSessionService.getUserSessions(USER_ID);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo("session-1");
    assertThat(result.get(1).getId()).isEqualTo("session-2");
    verify(chatSessionRepository).findByUserIdOrderByUpdatedAtDesc(USER_ID);
  }

  @Test
  void getSession_shouldReturnSessionWithMessages() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);
    session.setTitle(TITLE);

    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title(TITLE).build();

    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID))
        .thenReturn(Optional.of(session));
    when(chatSessionMapper.toDTOWithMessages(session)).thenReturn(sessionDTO);

    ChatSessionDTO result = chatSessionService.getSession(SESSION_ID, USER_ID);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(SESSION_ID);
    verify(chatSessionRepository).findByIdAndUserId(SESSION_ID, USER_ID);
    verify(chatSessionMapper).toDTOWithMessages(session);
  }

  @Test
  void getSession_shouldThrowExceptionWhenSessionNotFound() {
    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> chatSessionService.getSession(SESSION_ID, USER_ID))
        .isInstanceOf(BadRequestException.class)
        .hasMessage("Session not found");
  }

  @Test
  void updateSessionTitle_shouldUpdateAndReturnSession() {
    String newTitle = "Updated Chat";
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);
    session.setTitle(TITLE);

    ChatSession updatedSession = new ChatSession();
    updatedSession.setId(SESSION_ID);
    updatedSession.setUserId(USER_ID);
    updatedSession.setTitle(newTitle);

    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title(newTitle).build();

    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID))
        .thenReturn(Optional.of(session));
    when(chatSessionRepository.save(session)).thenReturn(updatedSession);
    when(chatSessionMapper.toDTOWithoutMessages(updatedSession)).thenReturn(sessionDTO);

    ChatSessionDTO result = chatSessionService.updateSessionTitle(SESSION_ID, USER_ID, newTitle);

    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo(newTitle);
    verify(chatSessionRepository).save(session);
    verify(chatSessionMapper).toDTOWithoutMessages(updatedSession);
  }

  @Test
  void updateSessionTitle_shouldKeepExistingTitleWhenNewTitleIsBlank() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);
    session.setTitle(TITLE);

    ChatSessionDTO sessionDTO = ChatSessionDTO.builder().id(SESSION_ID).title(TITLE).build();

    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID))
        .thenReturn(Optional.of(session));
    when(chatSessionRepository.save(session)).thenReturn(session);
    when(chatSessionMapper.toDTOWithoutMessages(session)).thenReturn(sessionDTO);

    ChatSessionDTO result = chatSessionService.updateSessionTitle(SESSION_ID, USER_ID, "  ");

    assertThat(result.getTitle()).isEqualTo(TITLE);
  }

  @Test
  void deleteSession_shouldDeleteExistingSession() {
    ChatSession session = new ChatSession();
    session.setId(SESSION_ID);
    session.setUserId(USER_ID);
    session.setTitle(TITLE);

    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID))
        .thenReturn(Optional.of(session));

    chatSessionService.deleteSession(SESSION_ID, USER_ID);

    verify(chatSessionRepository).delete(session);
  }

  @Test
  void deleteSession_shouldThrowExceptionWhenSessionNotFound() {
    when(chatSessionRepository.findByIdAndUserId(SESSION_ID, USER_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> chatSessionService.deleteSession(SESSION_ID, USER_ID))
        .isInstanceOf(BadRequestException.class)
        .hasMessage("Session not found");
  }
}
