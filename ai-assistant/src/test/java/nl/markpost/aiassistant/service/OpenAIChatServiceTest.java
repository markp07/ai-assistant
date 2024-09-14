package nl.markpost.aiassistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class OpenAIChatServiceTest {

  @Mock
  private OpenAiChatModel openAiChatModel;

  @Mock
  private OpenAIChatService.Assistant assistant;

  @InjectMocks
  private OpenAIChatService openAIChatService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ChatResponse response = Mockito.mock(ChatResponse.class);
//    when(response.()).thenReturn("Hello, World!");
    when(openAiChatModel.chat(any(ChatRequest.class))).thenReturn(response);
    when(assistant.chat(anyString())).thenReturn("Hello, World!");
  }

  @Test
  void testChat() {
    String input = "World";
    String expectedOutput = "Hello, World!";
    String actualOutput = openAIChatService.chat(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  void testChatStream() {
    String input = "World";

    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          openAIChatService.chatStream(input);
        });
  }
}