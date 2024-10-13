package nl.markpost.aiassistant.common.interfaces;

import nl.markpost.aiassistant.common.models.ChatInputDTO;
import nl.markpost.aiassistant.common.models.ChatOutputDTO;
import reactor.core.publisher.Flux;

public interface ChatService {

  ChatOutputDTO chat(ChatInputDTO input);

  Flux<String> chatStream(ChatInputDTO input);
}
