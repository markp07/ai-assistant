package nl.markpost.aiassistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;

class CustomChatMemoryStoreTest {

  private CustomChatMemoryStore customChatMemoryStore;
  private DB db;

  @BeforeEach
  void setUp() {
    db = DBMaker.memoryDB().make();
    customChatMemoryStore = new CustomChatMemoryStore();
  }

  @Test
  void getMessagesReturnsEmptyListWhenNoMessagesExist() {
    List<ChatMessage> messages = customChatMemoryStore.getMessages("nonexistent");
    assertEquals(Collections.emptyList(), messages);
  }

  @Test
  void getMessagesReturnsStoredMessages() {
    List<ChatMessage> expectedMessages = List.of(new UserMessage("Hello"));
    customChatMemoryStore.updateMessages("memory1", expectedMessages);
    List<ChatMessage> actualMessages = customChatMemoryStore.getMessages("memory1");
    assertEquals(expectedMessages, actualMessages);
  }

  @Test
  void updateMessagesStoresMessages() {
    List<ChatMessage> messages = List.of(new UserMessage("Hello"));
    customChatMemoryStore.updateMessages("memory1", messages);
    List<ChatMessage> storedMessages = customChatMemoryStore.getMessages("memory1");
    assertEquals(messages, storedMessages);
  }

  @Test
  void deleteMessagesRemovesMessages() {
    List<ChatMessage> messages = List.of(new UserMessage("Hello"));
    customChatMemoryStore.updateMessages("memory1", messages);
    customChatMemoryStore.deleteMessages("memory1");
    List<ChatMessage> storedMessages = customChatMemoryStore.getMessages("memory1");
    assertEquals(Collections.emptyList(), storedMessages);
  }

  @Test
  void deleteMessagesDoesNothingIfMemoryIdDoesNotExist() {
    customChatMemoryStore.deleteMessages("nonexistent");
    assertEquals(Collections.EMPTY_LIST, customChatMemoryStore.getMessages("nonexistent"));
  }
}