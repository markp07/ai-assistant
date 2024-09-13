package nl.markpost.aiassistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ChatServiceTest {

  @Test
  void testChat() {
    ChatService chatService = new ChatService();
    String input = "World";
    String expectedOutput = "Hello, World!";
    String actualOutput = chatService.chat(input);
    assertEquals(expectedOutput, actualOutput);
  }
}
