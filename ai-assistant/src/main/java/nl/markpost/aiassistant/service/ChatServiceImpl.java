package nl.markpost.aiassistant.service;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

  @Value("${openai.api-key}")
  private String apiKey;

  public String chat(String input) {
    OpenAiChatModel model = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
        .temperature(0.5)
        .build();


    return model.generate(input);
  }

  @Override
  public Flux<String> chatStream(String input) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
