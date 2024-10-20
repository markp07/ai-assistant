package nl.markpost.aiassistant.common.interfaces;

import nl.markpost.aiassistant.common.models.ChatInputDTO;
import nl.markpost.aiassistant.common.models.ChatOutputDTO;
import reactor.core.publisher.Flux;

public interface ChatService {

  void addSystemMessage(ChatInputDTO message);

  ChatOutputDTO sendUserMessage(ChatInputDTO input);

  Flux<String> chatStream(ChatInputDTO input);

  void clearHistory();
}
