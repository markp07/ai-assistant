package nl.markpost.aiassistant.service;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import nl.markpost.aiassistant.models.ChatInputDTO;
import nl.markpost.aiassistant.models.ChatOutputDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Implementation of ChatService using OpenAI's Assistant.
 */
@Service
public class OpenAIChatService implements ChatService {

  private final Assistant assistant;
  private final ChatMemory chatMemory;

  /**
   * Constructs an OpenAIChatService with the given Assistant and ChatMemory.
   *
   * @param assistant  the Assistant to handle chat interactions
   * @param chatMemory the ChatMemory to manage conversation history
   */
  public OpenAIChatService(Assistant assistant, ChatMemory chatMemory) {
    this.assistant = assistant;
    this.chatMemory = chatMemory;
  }

  /**
   * Sends a user message to the assistant and returns the response.
   *
   * @param chatInputDTO the input message from the user
   * @return the assistant's response wrapped in ChatOutputDTO
   */
  @Override
  public ChatOutputDTO sendUserMessage(ChatInputDTO chatInputDTO) {
    String response = assistant.chat(chatInputDTO.chat());
    return new ChatOutputDTO(response);
  }

  /**
   * Adds a system message to the chat memory.
   *
   * @param message the system message to add
   */
  @Override
  public void addSystemMessage(ChatInputDTO message) {
    chatMemory.add(SystemMessage.systemMessage(message.chat()));
  }

  /**
   * Streams chat responses from the assistant.
   *
   * @param input the input message from the user
   * @return a Flux stream of assistant responses
   */
  @Override
  public Flux<String> chatStream(ChatInputDTO input) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Clears the chat history from memory.
   */
  @Override
  public void clearHistory() {
    chatMemory.clear();
  }
}
