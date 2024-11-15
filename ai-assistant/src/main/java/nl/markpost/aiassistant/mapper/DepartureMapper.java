package nl.markpost.aiassistant.mapper;

import java.util.List;
import java.util.stream.Collectors;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.api.model.Journey;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.RouteStation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DepartureMapper {

  @Mapping(source = "externalDeparture.direction", target = "destination")
  @Mapping(source = "externalDeparture.product.number", target = "trainNumber")
  @Mapping(source = "externalDeparture.plannedDateTime", target = "departureTime")
  @Mapping(
      expression =
          "java((int)java.time.Duration.between(externalDeparture.getPlannedDateTime(), externalDeparture.getActualDateTime()).toMinutes())",
      target = "delay")
  @Mapping(source = "externalDeparture.actualTrack", target = "track")
  @Mapping(source = "externalDeparture.cancelled", target = "cancelled")
  @Mapping(source = "externalDeparture.product.shortCategoryName", target = "category")
  @Mapping(
      source = "externalDeparture.routeStations",
      target = "via",
      qualifiedByName = "mapRouteStationsToVia")
  @Mapping(
      expression =
          "java(externalDeparture.getMessages().isEmpty() ? null : externalDeparture.getMessages().get(0).getMessage())",
      target = "message")
  @Mapping(source = "journey.trainLength", target = "trainLength")
  @Mapping(source = "journey.numberOfSeats", target = "numberOfSeats")
  @Mapping(source = "journey.stockIdentifiers", target = "stockIdentifiers")
  @Mapping(source = "journey.stockImageUris", target = "stockImageUris")
  @Mapping(source = "journey.crowdForecast", target = "crowdForecast")
  Departure from(
      nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure externalDeparture,
      Journey journey);

  @Named("mapRouteStationsToVia")
  default List<String> mapRouteStationsToVia(List<RouteStation> routeStations) {
    return routeStations.stream().map(RouteStation::getMediumName).collect(Collectors.toList());
  }
}
