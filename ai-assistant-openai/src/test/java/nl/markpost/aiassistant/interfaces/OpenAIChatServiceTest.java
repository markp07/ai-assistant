package nl.markpost.aiassistant.interfaces;

import static org.mockito.Mockito.mock;

import dev.langchain4j.memory.ChatMemory;
import java.util.Collections;
import nl.markpost.aiassistant.common.models.ChatInputDTO;
import nl.markpost.aiassistant.common.models.ChatOutputDTO;
import nl.markpost.aiassistant.openai.service.Assistant;
import nl.markpost.aiassistant.openai.service.OpenAIChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class OpenAIChatServiceTest {

  private final Assistant assistant = mock(Assistant.class);

  private final ChatMemory chatMemory = mock(ChatMemory.class);

  private final OpenAIChatService openAIChatService = new OpenAIChatService(assistant, chatMemory);

  @Test
  void testChat() {
    Mockito.when(chatMemory.messages()).thenReturn(Collections.emptyList());
    Mockito.when(assistant.chat(ArgumentMatchers.anyString())).thenReturn("Hello, World!");

    String input = "World";
    String expectedOutput = "Hello, World!";
    ChatOutputDTO actualOutput = openAIChatService.chat(new ChatInputDTO(input));
    Assertions.assertEquals(expectedOutput, actualOutput.chat());
  }

  @Test
  void testChatStream() {
    ChatInputDTO input = new ChatInputDTO("World");

    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> {
          openAIChatService.chatStream(input);
        });
  }
}
