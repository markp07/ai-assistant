package nl.markpost.aiassistant.service;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;
import static org.mapdb.Serializer.STRING;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.util.List;
import java.util.Map;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * A custom implementation of ChatMemoryStore that uses MapDB for persistent storage of chat
 * messages.
 */
public class CustomChatMemoryStore implements ChatMemoryStore {

  private final DB db =
      DBMaker.fileDB("chat-memory.db").transactionEnable().fileLockDisable().make();
  private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

  /**
   * Retrieves chat messages associated with the given memory ID.
   *
   * @param memoryId the memory ID (expected to be a String)
   * @return a list of ChatMessage objects
   * @throws IllegalArgumentException if the memory ID is null or of an unsupported type
   */
  @Override
  public List<ChatMessage> getMessages(Object memoryId) {
    var id =
        switch (memoryId) {
          case String s -> s;
          case null -> throw new IllegalArgumentException("Memory ID cannot be null");
          default ->
              throw new IllegalArgumentException(
                  "Unsupported memory ID type: " + memoryId.getClass());
        };
    var json = map.get(id);
    return messagesFromJson(json);
  }

  /**
   * Updates chat messages associated with the given memory ID.
   *
   * @param memoryId the memory ID (expected to be a String)
   * @param messages the list of ChatMessage objects to store
   * @throws IllegalArgumentException if the memory ID is null or of an unsupported type
   */
  @Override
  public void updateMessages(Object memoryId, List<ChatMessage> messages) {
    var id =
        switch (memoryId) {
          case String s -> s;
          case null -> throw new IllegalArgumentException("Memory ID cannot be null");
          default ->
              throw new IllegalArgumentException(
                  "Unsupported memory ID type: " + memoryId.getClass());
        };
    var json = messagesToJson(messages);
    map.put(id, json);
    db.commit();
  }

  /**
   * Deletes chat messages associated with the given memory ID.
   *
   * @param memoryId the memory ID (expected to be a String)
   * @throws IllegalArgumentException if the memory ID is null or of an unsupported type
   */
  @Override
  public void deleteMessages(Object memoryId) {
    var id =
        switch (memoryId) {
          case String s -> s;
          case null -> throw new IllegalArgumentException("Memory ID cannot be null");
          default ->
              throw new IllegalArgumentException(
                  "Unsupported memory ID type: " + memoryId.getClass());
        };
    map.remove(id);
    db.commit();
  }
}
