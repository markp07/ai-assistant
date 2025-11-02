package nl.markpost.aiassistant.mapper;

import nl.markpost.aiassistant.api.model.ChatOutput;
import nl.markpost.aiassistant.models.ChatOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatOutputMapper {

  @Mapping(source = "chat", target = "chat")
  ChatOutput from(ChatOutputDTO chatInput);
}
