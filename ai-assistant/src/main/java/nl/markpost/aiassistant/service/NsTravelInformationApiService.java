package nl.markpost.aiassistant.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.client.NsTravelInformationClient;
import nl.markpost.aiassistant.constant.NSStationCode;
import nl.markpost.aiassistant.mapper.DepartureMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NsTravelInformationApiService {

  private final NsTravelInformationClient nsTravelInformationClient;

  private final DepartureMapper departureMapper;

  public @NotNull @Valid List<@Valid Departure> getDepartures(
      String station, Integer count, OffsetDateTime departureTime) {
    String stationCode = NSStationCode.getByName(station).getCode();

    List<nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure> departures =
        nsTravelInformationClient
            .getDepartures("nl", stationCode, departureTime.toString(), null, count)
            .getPayload()
            .getDepartures();

    return departureMapper.from(departures);
  }
}
