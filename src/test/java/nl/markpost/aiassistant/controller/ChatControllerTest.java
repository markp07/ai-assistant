package nl.markpost.aiassistant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import nl.markpost.aiassistant.api.model.ChatInput;
import nl.markpost.aiassistant.api.model.ChatOutput;
import nl.markpost.aiassistant.mapper.ChatInputDTOMapper;
import nl.markpost.aiassistant.mapper.ChatInputDTOMapperImpl;
import nl.markpost.aiassistant.mapper.ChatOutputMapper;
import nl.markpost.aiassistant.mapper.ChatOutputMapperImpl;
import nl.markpost.aiassistant.models.ChatInputDTO;
import nl.markpost.aiassistant.models.ChatOutputDTO;
import nl.markpost.aiassistant.service.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

  @Mock private ChatService chatService;

  @Spy private ChatInputDTOMapper chatInputDTOMapper = new ChatInputDTOMapperImpl();

  @Spy private ChatOutputMapper chatOutputMapper = new ChatOutputMapperImpl();

  @InjectMocks private ChatController chatController;

  @Test
  void testChatPost() {
    var chatInput = new ChatInput();
    chatInput.setChat("Hello");

    var chatOutput = new ChatOutput();
    chatOutput.setChat("Hello, world!");

    var chatOutputDto = new ChatOutputDTO("Hello, world!");

    when(chatService.sendUserMessage(any(ChatInputDTO.class))).thenReturn(chatOutputDto);

    var response = chatController.chatPost(chatInput);

    assertEquals(ResponseEntity.ok(chatOutput), response);
  }
}
