package nl.markpost.aiassistant.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

  public String chat(String input) {
    return "Hello, " + input + "!";
  }

}
