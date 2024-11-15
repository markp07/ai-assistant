package nl.markpost.aiassistant.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.api.model.Journey;
import nl.markpost.aiassistant.client.NsTravelInformationClient;
import nl.markpost.aiassistant.constant.NSStationCode;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.RepresentationResponseJourney;
import nl.markpost.aiassistant.mapper.DepartureMapper;
import nl.markpost.aiassistant.mapper.JourneyMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NsTravelInformationApiService {

  private final NsTravelInformationClient nsTravelInformationClient;

  private final DepartureMapper departureMapper;

  private final JourneyMapper journeyMapper;

  public List<Departure> getDepartures(
      String station, Integer count, OffsetDateTime departureTime) {
    String stationCode = NSStationCode.getByName(station).getCode();

    List<nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure>
        externalDepartures =
            nsTravelInformationClient
                .getDepartures("nl", stationCode, departureTime.toString(), null, count)
                .getPayload()
                .getDepartures();

    List<Departure> departures = new ArrayList<>();

    for (nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure
        externalDeparture : externalDepartures) {
      String trainNumber = externalDeparture.getProduct().getNumber();
      Journey journey = getJourney(trainNumber, station);
      Departure departure = departureMapper.from(externalDeparture, journey);
      departures.add(departure);
    }

    return departures;
  }

  public Journey getJourney(String trainNumber, String station) {
    RepresentationResponseJourney nsJourney = nsTravelInformationClient.getJourney(trainNumber);

    return journeyMapper.from(
        nsJourney.getPayload().getStops().stream()
            .filter(stop -> stop.getStop().getName().equals(station))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Station not found: " + station)),
        nsJourney.getPayload());
  }
}
