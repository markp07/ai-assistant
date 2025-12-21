package nl.markpost.aiassistant.mapper;

import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.models.MessageDTO;
import nl.markpost.aiassistant.models.entity.ChatMessage;
import nl.markpost.aiassistant.models.entity.ChatSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between ChatSession and ChatSessionDTO, as well as ChatMessage and
 * MessageDTO.
 */
@Mapper(componentModel = "spring")
public interface ChatSessionMapper {

  @Mapping(target = "messages", ignore = true)
  ChatSessionDTO toDTOWithoutMessages(ChatSession session);

  @Mapping(target = "messages", source = "messages")
  ChatSessionDTO toDTOWithMessages(ChatSession session);

  MessageDTO toMessageDTO(ChatMessage message);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "messages", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  ChatSession toChatSession(String userId, String title);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "timestamp", ignore = true)
  ChatMessage toChatMessage(ChatSession chatSession, String role, String content);
}
