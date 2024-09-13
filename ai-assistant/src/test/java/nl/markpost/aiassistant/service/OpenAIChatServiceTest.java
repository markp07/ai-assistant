package nl.markpost.aiassistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OpenAIChatServiceTest {

  @Test
  void testChat() {
    OpenAIChatService openAIChatService = new OpenAIChatService();
    String input = "World";
    String expectedOutput = "Hello, World!";
    String actualOutput = openAIChatService.chat(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  void testChatStream() {
    OpenAIChatService openAIChatService = new OpenAIChatService();
    String input = "World";

    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          openAIChatService.chatStream(input);
        });
  }
}
