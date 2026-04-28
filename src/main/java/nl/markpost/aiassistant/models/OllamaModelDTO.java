package nl.markpost.aiassistant.models;

import lombok.Builder;
import lombok.Data;

/** DTO representing an Ollama model. */
@Data
@Builder
public class OllamaModelDTO {

  private String name;
}
