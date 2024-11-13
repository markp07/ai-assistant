package nl.markpost.aiassistant.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.client.BeRailClient;
import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponseDeparturesDepartureInner;
import nl.markpost.aiassistant.mapper.DepartureMapper;
import nl.markpost.aiassistant.mapper.JourneyMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeRailApiService {

  private final BeRailClient beRailClient;

  private final DepartureMapper departureMapper;

  private final JourneyMapper journeyMapper;

  public List<Departure> getDepartures(String station) {
    station = station.replace(" ", "+");

    List<LiveboardResponseDeparturesDepartureInner>
        externalDepartures =
        beRailClient.getLiveBoard(station, "departure", true, "json", "nl").getDepartures()
            .getDeparture();
    ;

    List<Departure> departures = departureMapper.fromBeRail(externalDepartures);

    return departures;
  }

}
