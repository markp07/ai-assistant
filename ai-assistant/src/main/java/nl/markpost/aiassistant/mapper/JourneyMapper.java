package nl.markpost.aiassistant.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nl.markpost.aiassistant.api.model.Journey;
import nl.markpost.aiassistant.api.model.Journey.CrowdForecastEnum;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.JourneyStop;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.JourneyStop.StatusEnum;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.Part;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JourneyMapper {

  @Mapping(expression = "java(externalJourneyStop.getArrivals().get(0).getOrigin().getName())", target = "origin")
  @Mapping(source = "externalJourneyStop.destination", target = "destination")
  @Mapping(expression = "java(externalJourneyStop.getArrivals().get(0).getProduct().getShortCategoryName())", target = "category")
  @Mapping(expression = "java(externalJourneyStop.getArrivals().get(0).getCancelled())", target = "cancelled")
  @Mapping(source = "externalJourneyStop.actualStock.trainType", target = "trainType")
  @Mapping(source = "externalJourneyStop.actualStock.numberOfParts", target = "trainLength")
  @Mapping(source = "externalJourneyStop.actualStock.numberOfSeats", target = "numberOfSeats")
  @Mapping(expression = "java(externalJourneyStop.getArrivals().get(0).getStockIdentifiers())", target = "stockIdentifiers")
  @Mapping(source = "externalJourneyStop.actualStock.trainParts", target = "stockImageUris", qualifiedByName = "mapTrainPartsToImageUris")
  @Mapping(source = "externalJourneyStop", target = "crowdForecast", qualifiedByName = "mapCrowdForecastToCrowdForecastEnum")
  @Mapping(source = "externalJourney", target = "stops", qualifiedByName = "mapStops")
  Journey from(JourneyStop externalJourneyStop,
      nl.markpost.aiassistant.external.api.ns.travelinformation.model.Journey externalJourney);

  @Named("mapTrainPartsToImageUris")
  default List<String> mapTrainPartsToImageUris(List<Part> trainParts) {
    if (trainParts == null) {
      return Collections.emptyList();
    }
    return trainParts.stream()
        .map(part -> part.getImage().getUri())
        .collect(Collectors.toList());
  }

  @Named("mapCrowdForecastToCrowdForecastEnum")
  default CrowdForecastEnum mapCrowdForecastToCrowdForecastEnum(JourneyStop externalJourney) {
    return CrowdForecastEnum.fromValue(
        externalJourney.getArrivals().getFirst().getCrowdForecast().getValue());
  }

  @Named("mapStops")
  default List<String> mapStops(
      nl.markpost.aiassistant.external.api.ns.travelinformation.model.Journey externalJourney) {
    return externalJourney.getStops().stream()
        .filter(stop -> !stop.getStatus().equals(StatusEnum.PASSING))
        .map(stop -> stop.getStop().getName()).toList();
  }
}
