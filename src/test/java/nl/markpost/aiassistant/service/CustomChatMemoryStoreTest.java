package nl.markpost.aiassistant.service;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomChatMemoryStoreTest {

  private CustomChatMemoryStore customChatMemoryStore;

  @BeforeEach
  void setUp() {
    customChatMemoryStore = new CustomChatMemoryStore();
  }

  @Test
  void getMessagesReturnsEmptyListWhenNoMessagesExist() {
    var messages = customChatMemoryStore.getMessages("nonexistent");
    Assertions.assertEquals(Collections.emptyList(), messages);
  }

  @Test
  void getMessagesReturnsStoredMessages() {
    List<ChatMessage> expectedMessages = List.of(new UserMessage("Hello"));
    customChatMemoryStore.updateMessages("memory1", expectedMessages);
    var actualMessages = customChatMemoryStore.getMessages("memory1");
    Assertions.assertEquals(expectedMessages, actualMessages);
  }

  @Test
  void updateMessagesStoresMessages() {
    List<ChatMessage> messages = List.of(new UserMessage("Hello"));
    customChatMemoryStore.updateMessages("memory1", messages);
    var storedMessages = customChatMemoryStore.getMessages("memory1");
    Assertions.assertEquals(messages, storedMessages);
  }

  @Test
  void deleteMessagesRemovesMessages() {
    List<ChatMessage> messages = List.of(new UserMessage("Hello"));
    customChatMemoryStore.updateMessages("memory1", messages);
    customChatMemoryStore.deleteMessages("memory1");
    var storedMessages = customChatMemoryStore.getMessages("memory1");
    Assertions.assertEquals(Collections.emptyList(), storedMessages);
  }

  @Test
  void deleteMessagesDoesNothingIfMemoryIdDoesNotExist() {
    customChatMemoryStore.deleteMessages("nonexistent");
    var messages = customChatMemoryStore.getMessages("nonexistent");
    Assertions.assertEquals(Collections.emptyList(), messages);
  }
}
