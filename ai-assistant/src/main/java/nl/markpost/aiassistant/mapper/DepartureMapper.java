package nl.markpost.aiassistant.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponseDeparturesDepartureInner;
import nl.markpost.aiassistant.external.api.ns.travelinformation.model.RouteStation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DepartureMapper {

  @Mapping(source = "product.number", target = "trainNumber")
  @Mapping(source = "plannedDateTime", target = "departureTime")
  @Mapping(
      expression =
          "java(java.time.Duration.between(externalDeparture.getPlannedDateTime(), externalDeparture.getActualDateTime()).toMinutes())",
      target = "delay")
  @Mapping(source = "actualTrack", target = "track")
  @Mapping(source = "product.shortCategoryName", target = "category")
  @Mapping(source = "routeStations", target = "via", qualifiedByName = "mapRouteStationsToVia")
  @Mapping(
      expression =
          "java(externalDeparture.getMessages().isEmpty() ? null : externalDeparture.getMessages().get(0).getMessage())",
      target = "message")
  Departure from(
      nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure externalDeparture);

  List<Departure> from(
      List<nl.markpost.aiassistant.external.api.ns.travelinformation.model.Departure>
          externalDepartures);

  @Mapping(expression = "java(externalDeparture.getVehicleinfo().getShortname().split(\" \")[1])", target = "trainNumber")
  @Mapping(source = "stationinfo.standardname", target = "direction")
  @Mapping(source = "time", target = "departureTime", qualifiedByName = "mapBigDecimalToOffsetDateTime")
  @Mapping(expression = "java(externalDeparture.getDelay() / 60)", target = "delay")
  @Mapping(source = "platform", target = "track")
  @Mapping(expression = "java(externalDeparture.getCanceled() == 1)", target = "cancelled")
  @Mapping(expression = "java(externalDeparture.getVehicleinfo().getShortname().split(\" \")[0])", target = "category")
//  @Mapping(source = "routeStations", target = "via", qualifiedByName = "mapRouteStationsToVia")
//  @Mapping(
//      expression =
//          "java(externalDeparture.getMessages().isEmpty() ? null : externalDeparture.getMessages().get(0).getMessage())",
//      target = "message")
  Departure fromBeRail(LiveboardResponseDeparturesDepartureInner externalDeparture);

  List<Departure> fromBeRail(
      List<LiveboardResponseDeparturesDepartureInner>
          externalDepartures);

  @Named("mapRouteStationsToVia")
  default List<String> mapRouteStationsToVia(List<RouteStation> routeStations) {
    return routeStations.stream().map(RouteStation::getMediumName).collect(Collectors.toList());
  }

  @Named("mapBigDecimalToOffsetDateTime")
  default OffsetDateTime mapBigDecimalToOffsetDateTime(BigDecimal timestamp) {
    return OffsetDateTime.ofInstant(Instant.ofEpochSecond(timestamp.longValue()), ZoneOffset.ofHours(1));
  }
}
