package nl.markpost.aiassistant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Controller for handling chat-related API requests.
 * @deprecated Use ChatSessionController instead for session-based chat.
 */
@Slf4j
@Controller
@RequestMapping("/api/v1")
@AllArgsConstructor
@Deprecated
public class ChatController implements ChatApi {

  private final ChatService chatService;
  private final ChatInputDTOMapper chatInputDTOMapper;
  private final ChatOutputMapper chatOutputMapper;

  /**
   * Handles POST requests for chat messages.
   *
   * @param chatInput the chat input
   * @return the chat output response entity
   */
  @Override
  public ResponseEntity<ChatOutput> sendChatMessage(ChatInput chatInput) {
    log.info("Received chat message: {}", chatInput.getChat());
    ChatInputDTO chatInputDTO = chatInputDTOMapper.from(chatInput);
    ChatOutputDTO chatOutputDTO = chatService.sendUserMessage(chatInputDTO);
    ChatOutput chatOutput = chatOutputMapper.from(chatOutputDTO);

    return ResponseEntity.ok(chatOutput);
  }

  /**
   * Handles DELETE requests to clear chat history.
   *
   * @return a response entity with no content
   */
  @Override
  public ResponseEntity<Void> clearChatHistory() {
    chatService.clearHistory();
    return ResponseEntity.noContent().build();
  }
}
