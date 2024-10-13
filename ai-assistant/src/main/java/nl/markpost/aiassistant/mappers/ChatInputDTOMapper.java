package nl.markpost.aiassistant.mappers;

import nl.markpost.aiassistant.api.model.ChatInput;
import nl.markpost.aiassistant.common.models.ChatInputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatInputDTOMapper {

  @Mapping(source = "chat", target = "chat")
  ChatInputDTO from(ChatInput chatInput);
}
