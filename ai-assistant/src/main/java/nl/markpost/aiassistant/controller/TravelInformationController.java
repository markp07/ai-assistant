package nl.markpost.aiassistant.controller;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.controller.TravelApi;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.api.model.Journey;
import nl.markpost.aiassistant.service.BeRailApiService;
import nl.markpost.aiassistant.service.NsTravelInformationApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TravelInformationController implements TravelApi {

  private final NsTravelInformationApiService nsTravelInformationApiService;
  private final BeRailApiService beRailApiService;

  @Override
  public ResponseEntity<List<Departure>> travelDeparturesGet(
      String station,
      Integer count,
      OffsetDateTime departureTime,
      Boolean addJourneys,
      String country) {

    if (count == null) {
      count = 5;
    }

    if (departureTime == null) {
      departureTime = OffsetDateTime.now();
    }

    List<Departure> departures =
        switch (country) {
          case "be" -> beRailApiService.getDepartures(station, count);
          case "nl" -> nsTravelInformationApiService.getDepartures(station, count, departureTime);
          default -> throw new IllegalArgumentException("Country not found: " + country);
        };

    return ResponseEntity.ok(departures);
  }

  @Override
  public ResponseEntity<Journey> travelJourneyGet(String station, String trainNumber) {
    return ResponseEntity.ok(nsTravelInformationApiService.getJourney(trainNumber, station));
  }
}
