package nl.markpost.aiassistant.service;

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

    //TODO: create chat history

    Assistant assistant = AiServices.builder(Assistant.class).chatLanguageModel(model).build();

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
