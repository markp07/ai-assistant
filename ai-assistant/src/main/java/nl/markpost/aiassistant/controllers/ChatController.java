package nl.markpost.aiassistant.controllers;

import nl.markpost.aiassistant.api.controller.ChatApi;
import nl.markpost.aiassistant.api.model.ChatInput;
import nl.markpost.aiassistant.api.model.ChatOutput;
import nl.markpost.aiassistant.common.interfaces.ChatService;
import nl.markpost.aiassistant.common.models.ChatInputDTO;
import nl.markpost.aiassistant.common.models.ChatOutputDTO;
import nl.markpost.aiassistant.mappers.ChatInputDTOMapper;
import nl.markpost.aiassistant.mappers.ChatOutputMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class ChatController implements ChatApi {

  private final ChatService chatService;

  private final ChatInputDTOMapper chatInputDTOMapper;

  private final ChatOutputMapper chatOutputMapper;

  @Autowired
  public ChatController(
      ChatService chatService,
      ChatInputDTOMapper chatInputDTOMapper,
      ChatOutputMapper chatOutputMapper) {
    this.chatService = chatService;
    this.chatInputDTOMapper = chatInputDTOMapper;
    this.chatOutputMapper = chatOutputMapper;
  }

  @Override
  public ResponseEntity<ChatOutput> chatPost(ChatInput chatInput) {
    ChatInputDTO chatInputDTO = chatInputDTOMapper.from(chatInput);
    ChatOutputDTO chatOutputDTO = chatService.chat(chatInputDTO);
    ChatOutput chatOutput = chatOutputMapper.from(chatOutputDTO);

    return ResponseEntity.ok(chatOutput);
  }
}
