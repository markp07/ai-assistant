package nl.markpost.aiassistant.client;

import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponse;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.RepresentationResponseJourney;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "BeRailClient",
    url = "https://api.irail.be")
public interface BeRailClient {

  @GetMapping("/liveboard")
  LiveboardResponse getLiveBoard(
      @RequestParam("station") String station,
      @RequestParam(value = "arrdep", defaultValue = "departure") String arrdep,
      @RequestParam(value = "alerts", defaultValue = "true") boolean alerts,
      @RequestParam(value = "format", defaultValue = "json") String format,
      @RequestParam(value = "lang", defaultValue = "nl") String lang);

  @GetMapping("/api/v2/journey")
  RepresentationResponseJourney getJourney(@RequestParam("train") String trainNumber);
}
