package nl.markpost.aiassistant.service;

import nl.markpost.aiassistant.models.ChatInputDTO;
import nl.markpost.aiassistant.models.ChatOutputDTO;
import reactor.core.publisher.Flux;

public interface ChatService {

  void addSystemMessage(ChatInputDTO message);

  ChatOutputDTO sendUserMessage(ChatInputDTO input);

  Flux<String> chatStream(ChatInputDTO input);

  void clearHistory();
}
