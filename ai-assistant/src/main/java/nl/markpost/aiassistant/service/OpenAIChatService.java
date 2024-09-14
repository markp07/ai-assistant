package nl.markpost.aiassistant.service;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class OpenAIChatService implements ChatService {

  @Value("${openai.api-key}")
  private String apiKey;

  public String chat(String message) {
    OpenAiChatModel model = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
        .temperature(0.5)
        .build();

    ChatMemory memory = MessageWindowChatMemory.builder().maxMessages(10)
        .chatMemoryStore(new CustomChatMemoryStore()).build();

    if (memory.messages().isEmpty()) {
      memory.add(SystemMessage.systemMessage(
          "You are a personal assistant. You are here to help answering questions and retrieve information. Keep your answers short and to the point unless asked otherwise.If asked for the weather, don't make up a response, but search it online."));
    }

    Assistant assistant = AiServices.builder(Assistant.class).chatLanguageModel(model).chatMemory(memory).build();

    return assistant.chat(message);
  }

  @Override
  public Flux<String> chatStream(String input) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  interface Assistant {

    String chat(String message);
  }
}
