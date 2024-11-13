package nl.markpost.aiassistant.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.client.BeRailClient;
import nl.markpost.aiassistant.external.api.berail.model.CompositionResponse;
import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponseDeparturesDepartureInner;
import nl.markpost.aiassistant.external.api.berail.model.VehicleResponse;
import nl.markpost.aiassistant.mapper.BeRailMapper;
import nl.markpost.aiassistant.mapper.JourneyMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeRailApiService {

  private final BeRailClient beRailClient;

  private final BeRailMapper beRailMapper;

  private final JourneyMapper journeyMapper;

  @SneakyThrows
  public List<Departure> getDepartures(String station) {
    station = station.replace(" ", "+");

    List<LiveboardResponseDeparturesDepartureInner> nmbsDepartures =
        beRailClient
            .getLiveBoard(station, "departure", true, "json", "nl")
            .getDepartures()
            .getDeparture();

    nmbsDepartures = nmbsDepartures.size() > 10 ? nmbsDepartures.subList(0, 9) : nmbsDepartures;
    List<Departure> departures = new ArrayList<>();

    for (LiveboardResponseDeparturesDepartureInner nmbsDeparture : nmbsDepartures) {
      VehicleResponse vehicleResponse =
          beRailClient.getVehicle(nmbsDeparture.getVehicle(), "json", "nl");
      CompositionResponse compositionResponse =
          beRailClient.getComposition(nmbsDeparture.getVehicle(), "json", "nl", "");

      Departure departure =
          beRailMapper.mapDeparture(station, nmbsDeparture, vehicleResponse, compositionResponse);
      departures.add(departure);

      Thread.sleep(1000);
    }

    return departures;
  }
}
