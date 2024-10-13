package nl.markpost.aiassistant.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.markpost.aiassistant.api.model.ChatInput;
import nl.markpost.aiassistant.common.models.ChatInputDTO;
import org.junit.jupiter.api.Test;

class ChatInputDTOMapperTest {

  private final ChatInputDTOMapper chatInputDTOMapper = new ChatInputDTOMapperImpl();

  @Test
  void testFrom() {
    ChatInput chatInput = new ChatInput();
    chatInput.setChat("Test chat");

    ChatInputDTO chatInputDTO = chatInputDTOMapper.from(chatInput);

    assertNotNull(chatInputDTO);
    assertEquals("Test chat", chatInputDTO.chat());
  }
}
