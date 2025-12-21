package nl.markpost.aiassistant.mapper;

import nl.markpost.aiassistant.api.model.ChatSession;
import nl.markpost.aiassistant.api.model.Message;
import nl.markpost.aiassistant.models.ChatSessionDTO;
import nl.markpost.aiassistant.models.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Mapper interface for converting between ChatSessionDTO/MessageDTO and ChatSession/Message API models.
 */
@Mapper(componentModel = "spring")
public interface SessionApiMapper {

    @Mapping(target = "createdAt", expression = "java(toOffsetDateTime(dto.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(toOffsetDateTime(dto.getUpdatedAt()))")
    ChatSession toApiModel(ChatSessionDTO dto);

    List<ChatSession> toApiModelList(List<ChatSessionDTO> dtos);

    @Mapping(target = "timestamp", expression = "java(toOffsetDateTime(dto.getTimestamp()))")
    @Mapping(target = "role", expression = "java(mapRole(dto.getRole()))")
    Message toApiModel(MessageDTO dto);

    List<Message> toApiMessageList(List<MessageDTO> dtos);

    default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(ZoneOffset.UTC) : null;
    }

    default Message.RoleEnum mapRole(String role) {
        if (role == null) {
            return null;
        }
        // Use fromValue() which expects the lowercase string value
        return Message.RoleEnum.fromValue(role.toLowerCase());
    }
}

