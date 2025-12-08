package nl.markpost.aiassistant.controller;

import nl.markpost.aiassistant.api.controller.ChatApi;
import nl.markpost.aiassistant.api.model.ChatInput;
import nl.markpost.aiassistant.api.model.ChatOutput;
import nl.markpost.aiassistant.mapper.ChatInputDTOMapper;
import nl.markpost.aiassistant.mapper.ChatOutputMapper;
import nl.markpost.aiassistant.models.ChatInputDTO;
import nl.markpost.aiassistant.models.ChatOutputDTO;
import nl.markpost.aiassistant.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class ChatController implements ChatApi {

  private final ChatService chatService;

  private final ChatInputDTOMapper chatInputDTOMapper;

  private final ChatOutputMapper chatOutputMapper;

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
    ChatOutputDTO chatOutputDTO = chatService.sendUserMessage(chatInputDTO);
    ChatOutput chatOutput = chatOutputMapper.from(chatOutputDTO);

    return ResponseEntity.ok(chatOutput);
  }

  @Override
  public ResponseEntity<Void> chatDelete() {
    chatService.clearHistory();
    return ResponseEntity.noContent().build();
  }
}
