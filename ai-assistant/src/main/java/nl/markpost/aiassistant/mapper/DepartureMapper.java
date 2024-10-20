package nl.markpost.aiassistant.mapper;

import java.util.List;
import java.util.stream.Collectors;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.RouteStation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DepartureMapper {

  @Mapping(source = "plannedDateTime", target = "departureTime")
  @Mapping(
      expression =
          "java(java.time.Duration.between(externalDeparture.getPlannedDateTime(), externalDeparture.getActualDateTime()).toMinutes())",
      target = "delay")
  @Mapping(source = "actualTrack", target = "track")
  @Mapping(source = "product.longCategoryName", target = "category")
  @Mapping(source = "routeStations", target = "via", qualifiedByName = "mapRouteStationsToVia")
  Departure from(
      nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure externalDeparture);

  List<Departure> from(
      List<nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure>
          externalDepartures);

  @Named("mapRouteStationsToVia")
  default List<String> mapRouteStationsToVia(List<RouteStation> routeStations) {
    return routeStations.stream()
        .map(RouteStation::getMediumName)
        .collect(Collectors.toList());
  }
}
