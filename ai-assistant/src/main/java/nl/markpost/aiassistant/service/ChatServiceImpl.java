package nl.markpost.aiassistant.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

  public String chat(String input) {
    return "Hello, " + input + "!";
  }

  @Override
  public Flux<String> chatStream(String input) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
