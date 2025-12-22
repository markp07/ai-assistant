package nl.markpost.aiassistant.service;

import dev.langchain4j.service.TokenStream;

public interface Assistant {

  String chat(String message);

  TokenStream chatStream(String message);
}
