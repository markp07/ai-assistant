package nl.markpost.aiassistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ChatServiceImplTest {

  @Test
  void testChat() {
    ChatServiceImpl chatServiceImpl = new ChatServiceImpl();
    String input = "World";
    String expectedOutput = "Hello, World!";
    String actualOutput = chatServiceImpl.chat(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  void testChatStream() {
    ChatServiceImpl chatServiceImpl = new ChatServiceImpl();
    String input = "World";

    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          chatServiceImpl.chatStream(input);
        });
  }
}
