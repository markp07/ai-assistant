package nl.markpost.aiassistant.controller;

import nl.markpost.aiassistant.api.ChatApi;
import nl.markpost.aiassistant.model.ChatInput;
import nl.markpost.aiassistant.model.ChatOutput;
import nl.markpost.aiassistant.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class ChatController implements ChatApi {

  private final ChatService chatService;

  @Autowired
  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @Override
  public ResponseEntity<ChatOutput> chatPost(ChatInput chatInput) {
    // TODO: create mapper!
    return ResponseEntity.ok(new ChatOutput().chat(chatService.chat(chatInput.getChat())));
  }
}
