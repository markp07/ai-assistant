package nl.markpost.aiassistant.openai.service;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import nl.markpost.aiassistant.common.interfaces.ChatService;
import nl.markpost.aiassistant.common.models.ChatInputDTO;
import nl.markpost.aiassistant.common.models.ChatOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class OpenAIChatService implements ChatService {

  private final Assistant assistant;
  private final ChatMemory chatMemory;

  @Autowired
  public OpenAIChatService(Assistant assistant, ChatMemory chatMemory) {
    this.assistant = assistant;
    this.chatMemory = chatMemory;
  }

  public ChatOutputDTO chat(ChatInputDTO chatInputDTO) {
    if (chatMemory.messages().isEmpty()) {
      chatMemory.add(
          SystemMessage.systemMessage(
              "You are a personal assistant. You are here to help answering questions and retrieve information. Keep your answers short and to the point unless asked otherwise.If asked for the weather, don't make up a response, but search it online."));
    }

    String response = assistant.chat(chatInputDTO.chat());

    return new ChatOutputDTO(response);
  }

  @Override
  public Flux<String> chatStream(ChatInputDTO input) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
