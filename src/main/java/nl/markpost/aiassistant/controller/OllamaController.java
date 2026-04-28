package nl.markpost.aiassistant.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.controller.OllamaApi;
import nl.markpost.aiassistant.api.model.OllamaModel;
import nl.markpost.aiassistant.models.OllamaModelDTO;
import nl.markpost.aiassistant.service.OllamaModelsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for Ollama-related endpoints. Implements the generated OllamaApi interface from
 * OpenAPI specification.
 */
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OllamaController implements OllamaApi {

  private final OllamaModelsService ollamaModelsService;

  /**
   * Returns the list of models available on the local Ollama instance.
   *
   * @return a ResponseEntity containing a list of OllamaModel objects
   */
  @Override
  public ResponseEntity<List<OllamaModel>> getOllamaModels() {
    List<OllamaModelDTO> dtos = ollamaModelsService.getModels();
    List<OllamaModel> models =
        dtos.stream().map(dto -> OllamaModel.builder().name(dto.getName()).build()).toList();
    return ResponseEntity.ok(models);
  }
}
