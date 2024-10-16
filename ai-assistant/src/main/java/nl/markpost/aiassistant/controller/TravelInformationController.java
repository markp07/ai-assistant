package nl.markpost.aiassistant.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.constant.NSStationCode;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure;
import nl.markpost.aiassistant.service.NsTravelInformationApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/travel")
@RequiredArgsConstructor
// TODO: create API in API Spec and generate contorller interface
public class TravelInformationController {

  private final NsTravelInformationApiService nsTravelInformationApiService;

  @GetMapping("/departures")
  public ResponseEntity<List<Departure>> getDepartures(@RequestParam("station") String station) {
    // TODO: add input validation
    // TODO: add error handling

    List<Departure> departures =
        nsTravelInformationApiService.getDepartures(NSStationCode.getByName(station).getCode());

    return ResponseEntity.ok(departures);
  }
}
