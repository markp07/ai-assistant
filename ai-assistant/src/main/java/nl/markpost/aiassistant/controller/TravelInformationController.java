package nl.markpost.aiassistant.controller;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.controller.TravelApi;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.api.model.Journey;
import nl.markpost.aiassistant.service.NsTravelInformationApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TravelInformationController implements TravelApi {

  private final NsTravelInformationApiService nsTravelInformationApiService;

  @Override
  public ResponseEntity<List<Departure>> travelDeparturesGet(
      String station, Integer count, OffsetDateTime departureTime, Boolean addJourneys) {

    if (count == null) {
      count = 5;
    }

    if (departureTime == null) {
      departureTime = OffsetDateTime.now();
    }

    return ResponseEntity.ok(
        nsTravelInformationApiService.getDepartures(station, count, departureTime));
  }

  @Override
  public ResponseEntity<Journey> travelJourneyGet(String station, String trainNumber) {
    return ResponseEntity.ok(nsTravelInformationApiService.getJourney(trainNumber, station));
  }
}
