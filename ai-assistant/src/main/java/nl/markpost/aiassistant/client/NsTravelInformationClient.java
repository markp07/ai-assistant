package nl.markpost.aiassistant.client;

import nl.markpost.aiassistant.client.config.NsTravelInformationClientConfig;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.RepresentationResponseDeparturesPayload;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.RepresentationResponseJourney;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "nsTravelInformationClient",
    url = "https://gateway.apiportal.ns.nl/reisinformatie-api",
    configuration = NsTravelInformationClientConfig.class)
public interface NsTravelInformationClient {

  @GetMapping("/api/v2/departures")
  RepresentationResponseDeparturesPayload getDepartures(
      @RequestParam("lang") String lang,
      @RequestParam("station") String station,
      @RequestParam("uicCode") String uicCode,
      @RequestParam("dateTime") String dateTime,
      @RequestParam("maxJourneys") Integer maxJourneys);

  @GetMapping("/api/v2/journey")
  RepresentationResponseJourney getJourney(@RequestParam("train") Integer train);
}

