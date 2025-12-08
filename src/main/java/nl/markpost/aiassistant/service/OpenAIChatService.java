package nl.markpost.aiassistant.service;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import nl.markpost.aiassistant.models.ChatInputDTO;
import nl.markpost.aiassistant.models.ChatOutputDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class OpenAIChatService implements ChatService {

  private final Assistant assistant;
  private final ChatMemory chatMemory;

  public OpenAIChatService(Assistant assistant, ChatMemory chatMemory) {
    this.assistant = assistant;
    this.chatMemory = chatMemory;
  }

  @Override
  public ChatOutputDTO sendUserMessage(ChatInputDTO chatInputDTO) {
    String response = assistant.chat(chatInputDTO.chat());
    return new ChatOutputDTO(response);
  }

  @Override
  public void addSystemMessage(ChatInputDTO message) {
    chatMemory.add(SystemMessage.systemMessage(message.chat()));
  }

  @Override
  public Flux<String> chatStream(ChatInputDTO input) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void clearHistory() {
    chatMemory.clear();
  }
}
