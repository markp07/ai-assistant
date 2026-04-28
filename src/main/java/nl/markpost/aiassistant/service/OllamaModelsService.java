package nl.markpost.aiassistant.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.markpost.aiassistant.models.OllamaModelDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/** Service for fetching available models from the local Ollama instance. */
@Service
@Slf4j
public class OllamaModelsService {

  private final WebClient webClient;

  public OllamaModelsService(@Value("${ollama.base-url}") String ollamaBaseUrl) {
    this.webClient = WebClient.builder().baseUrl(ollamaBaseUrl).build();
  }

  /**
   * Fetches the list of available models from the Ollama instance.
   *
   * @return List of Ollama model DTOs.
   */
  public List<OllamaModelDTO> getModels() {
    OllamaTagsResponse response =
        webClient.get().uri("/api/tags").retrieve().bodyToMono(OllamaTagsResponse.class).block();
    if (response == null || response.getModels() == null) {
      return List.of();
    }
    return response.getModels().stream()
        .map(m -> OllamaModelDTO.builder().name(m.getName()).build())
        .toList();
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class OllamaTagsResponse {
    @JsonProperty("models")
    private List<OllamaModelEntry> models;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class OllamaModelEntry {
    @JsonProperty("name")
    private String name;
  }
}
