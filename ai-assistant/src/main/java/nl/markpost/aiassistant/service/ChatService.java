package nl.markpost.aiassistant.service;

import reactor.core.publisher.Flux;

public interface ChatService {

  String chat(String input);

  Flux<String> chatStream(String input);
}
