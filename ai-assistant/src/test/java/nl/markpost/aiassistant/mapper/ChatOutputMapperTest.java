package nl.markpost.aiassistant.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.markpost.aiassistant.api.model.ChatOutput;
import nl.markpost.aiassistant.common.models.ChatOutputDTO;
import org.junit.jupiter.api.Test;

class ChatOutputMapperTest {

  private final ChatOutputMapper chatOutputMapper = new ChatOutputMapperImpl();

  @Test
  void testFrom() {
    ChatOutputDTO chatOutputDTO = new ChatOutputDTO("Test chat");

    ChatOutput chatOutput = chatOutputMapper.from(chatOutputDTO);

    assertNotNull(chatOutput);
    assertEquals("Test chat", chatOutputDTO.chat());
  }
}
