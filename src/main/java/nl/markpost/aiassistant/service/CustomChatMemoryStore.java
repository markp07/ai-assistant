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

public class CustomChatMemoryStore implements ChatMemoryStore {

  private final DB db =
      DBMaker.fileDB("chat-memory.db").transactionEnable().fileLockDisable().make();
  private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

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
