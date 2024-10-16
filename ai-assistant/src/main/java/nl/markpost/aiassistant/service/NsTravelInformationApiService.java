package nl.markpost.aiassistant.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.client.NsTravelInformationClient;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NsTravelInformationApiService {

  private final NsTravelInformationClient nsTravelInformationClient;

  public List<Departure> getDepartures(String station) {
    // TODO: add error handling
    // TODO: add mappers
    return nsTravelInformationClient
        .getDepartures("nl", station, null, LocalDate.now().toString(), 5)
        .getPayload()
        .getDepartures();
  }
}
