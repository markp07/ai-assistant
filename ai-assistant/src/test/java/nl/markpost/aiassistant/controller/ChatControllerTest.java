package nl.markpost.aiassistant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import nl.markpost.aiassistant.interfaces.ChatService;
import nl.markpost.aiassistant.model.ChatInput;
import nl.markpost.aiassistant.model.ChatOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class ChatControllerTest {

  @Mock private ChatService chatService;

  @InjectMocks private ChatController chatController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testChatPost() {
    ChatInput chatInput = new ChatInput();
    chatInput.setChat("Hello");

    ChatOutput chatOutput = new ChatOutput();
    chatOutput.setChat("Hello, world!");

    when(chatService.chat("Hello")).thenReturn("Hello, world!");

    ResponseEntity<ChatOutput> response = chatController.chatPost(chatInput);

    assertEquals(ResponseEntity.ok(chatOutput), response);
  }
}
