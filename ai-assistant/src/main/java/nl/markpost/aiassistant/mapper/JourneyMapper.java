package nl.markpost.aiassistant.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nl.markpost.aiassistant.api.model.Journey;
import nl.markpost.aiassistant.api.model.Journey.CrowdForecastEnum;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.JourneyStop;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.JourneyStop.StatusEnum;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.Part;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.Stock;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapper {

  public Journey from(
      JourneyStop externalJourneyStop,
      nl.markpost.aiassistant.external.api.ns.travelinformation.model.Journey externalJourney) {
    if (externalJourneyStop == null && externalJourney == null) {
      return null;
    }

    Journey journey = new Journey();

    if (externalJourneyStop != null) {
      journey.setDestination(externalJourneyStop.getDestination());
      journey.setTrainType(externalJourneyStopActualStockTrainType(externalJourneyStop));
      Integer numberOfParts = externalJourneyStopActualStockNumberOfParts(externalJourneyStop);
      if (numberOfParts != null) {
        journey.setTrainLength(numberOfParts.longValue());
      }
      Integer numberOfSeats = externalJourneyStopActualStockNumberOfSeats(externalJourneyStop);
      if (numberOfSeats != null) {
        journey.setNumberOfSeats(numberOfSeats.longValue());
      }
      List<Part> trainParts = externalJourneyStopActualStockTrainParts(externalJourneyStop);
      journey.setStockImageUris(mapTrainPartsToImageUris(trainParts));
      journey.setCrowdForecast(mapCrowdForecastToCrowdForecastEnum(externalJourneyStop));
    }
    journey.setStops(mapStops(externalJourney));

    var arrival = externalJourneyStop.getArrivals().stream().findFirst().orElse(null);
    var departure = externalJourneyStop.getDepartures().stream().findFirst().orElse(null);

    if (arrival != null) {
      journey.setOrigin(arrival.getOrigin().getName());
      journey.setCategory(arrival.getProduct().getShortCategoryName());
      journey.setCancelled(arrival.getCancelled());
      journey.setStockIdentifiers(
          arrival.getStockIdentifiers().stream()
              .filter(identifier -> identifier != null && !identifier.equals("0"))
              .collect(Collectors.toList()));
    } else if (departure != null) {
      journey.setOrigin(departure.getOrigin().getName());
      journey.setCategory(departure.getProduct().getShortCategoryName());
      journey.setCancelled(departure.getCancelled());
      journey.setStockIdentifiers(
          departure.getStockIdentifiers().stream()
              .filter(identifier -> identifier != null && !identifier.equals("0"))
              .collect(Collectors.toList()));
    }

    return journey;
  }

  private String externalJourneyStopActualStockTrainType(JourneyStop journeyStop) {
    Stock actualStock = journeyStop.getActualStock();
    if (actualStock == null) {
      return null;
    }
    return actualStock.getTrainType();
  }

  private Integer externalJourneyStopActualStockNumberOfParts(JourneyStop journeyStop) {
    Stock actualStock = journeyStop.getActualStock();
    if (actualStock == null) {
      return null;
    }
    return actualStock.getNumberOfParts();
  }

  private Integer externalJourneyStopActualStockNumberOfSeats(JourneyStop journeyStop) {
    Stock actualStock = journeyStop.getActualStock();
    if (actualStock == null) {
      return null;
    }
    return actualStock.getNumberOfSeats();
  }

  private List<Part> externalJourneyStopActualStockTrainParts(JourneyStop journeyStop) {
    Stock actualStock = journeyStop.getActualStock();
    if (actualStock == null) {
      return null;
    }
    return actualStock.getTrainParts();
  }

  private List<String> mapTrainPartsToImageUris(List<Part> trainParts) {
    if (trainParts == null) {
      return Collections.emptyList();
    }
    return trainParts.stream()
        .map(part -> part.getImage() != null ? part.getImage().getUri() : null)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private CrowdForecastEnum mapCrowdForecastToCrowdForecastEnum(JourneyStop externalJourney) {
    if (externalJourney.getArrivals().isEmpty()) {
      return CrowdForecastEnum.UNKNOWN;
    }
    return CrowdForecastEnum.fromValue(
        externalJourney.getArrivals().getFirst().getCrowdForecast().getValue());
  }

  private List<String> mapStops(
      nl.markpost.aiassistant.external.api.ns.travelinformation.model.Journey externalJourney) {
    return externalJourney.getStops().stream()
        .filter(stop -> !stop.getStatus().equals(StatusEnum.PASSING))
        .map(stop -> stop.getStop().getName())
        .toList();
  }
}
