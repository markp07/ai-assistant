package nl.markpost.aiassistant.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.SneakyThrows;
import nl.markpost.aiassistant.exception.NotFoundException;
import nl.markpost.aiassistant.external.api.berail.model.CompositionResponse;
import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponse;
import nl.markpost.aiassistant.external.api.berail.model.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//TODO: add user agent header
@FeignClient(name = "BeRailClient", url = "https://api.irail.be")
public interface BeRailClient {

  @GetMapping("/v1/liveboard/")
  LiveboardResponse getLiveBoard(
      @RequestParam("station") String station,
      @RequestParam(value = "arrdep", defaultValue = "departure") String arrdep,
      @RequestParam(value = "alerts", defaultValue = "true") boolean alerts,
      @RequestParam(value = "format", defaultValue = "json") String format,
      @RequestParam(value = "lang", defaultValue = "nl") String lang);

  @GetMapping("/v1/vehicles/")
  @CircuitBreaker(name = "beRailClientGetVehicle", fallbackMethod = "fallbackGetVehicle")
  VehicleResponse getVehicle(
      @RequestParam("id") String id,
      @RequestParam(value = "format", defaultValue = "json") String format,
      @RequestParam(value = "lang", defaultValue = "nl") String lang);

  @SneakyThrows
  default VehicleResponse fallbackGetVehicle(Throwable exception) {
    if (exception instanceof NotFoundException) {
      return new VehicleResponse();
    } else {
      throw exception;
    }
  }

  @GetMapping("/v1/composition/")
  @CircuitBreaker(name = "beRailClientGetComposition", fallbackMethod = "fallbackGetComposition")
  CompositionResponse getComposition(
      @RequestParam("id") String id,
      @RequestParam(value = "format", defaultValue = "json") String format,
      @RequestParam(value = "lang", defaultValue = "nl") String lang,
      @RequestParam(value = "data", defaultValue = "") String data);

  @SneakyThrows
  default CompositionResponse fallbackGetComposition(Exception exception) {
    if (exception instanceof NotFoundException) {
      return new CompositionResponse();
    } else {
      throw exception;
    }
  }
}
