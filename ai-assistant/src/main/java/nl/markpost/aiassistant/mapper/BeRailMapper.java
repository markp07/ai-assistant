package nl.markpost.aiassistant.mapper;

import jakarta.validation.Valid;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.api.model.Departure.CrowdForecastEnum;
import nl.markpost.aiassistant.api.model.DepartureStatusEnum;
import nl.markpost.aiassistant.external.api.berail.model.CompositionResponse;
import nl.markpost.aiassistant.external.api.berail.model.CompositionResponseCompositionSegmentsSegmentInner;
import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponseDeparturesDepartureInner;
import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponseDeparturesDepartureInnerStationinfo;
import nl.markpost.aiassistant.external.api.berail.model.VehicleResponse;
import org.springframework.stereotype.Service;

@Service
public class BeRailMapper {

  public Departure mapDeparture(
      String station,
      LiveboardResponseDeparturesDepartureInner beRailDeparture,
      VehicleResponse vehicleResponse,
      CompositionResponse compositionResponse) {
    Departure departure = new Departure();

    List<@Valid CompositionResponseCompositionSegmentsSegmentInner> segments =
        Optional.ofNullable(compositionResponse.getComposition())
            .map(composition -> composition.getSegments().getSegment())
            .orElse(List.of());

    departure.setTrainNumber(
        Optional.ofNullable(beRailDeparture.getVehicleinfo())
            .map(vehicleInfo -> vehicleInfo.getShortname().split(" ")[1])
            .orElse(null));
    departure.setDestination(
        Optional.ofNullable(beRailDeparture.getStationinfo())
            .map(LiveboardResponseDeparturesDepartureInnerStationinfo::getStandardname)
            .orElse(null));
    departure.setDepartureTime(
        Optional.ofNullable(beRailDeparture.getTime())
            .map(
                time ->
                    OffsetDateTime.ofInstant(
                        Instant.ofEpochSecond(time.longValue()), ZoneOffset.ofHours(1)))
            .orElse(null));
    departure.setDelay(
        Optional.ofNullable(beRailDeparture.getDelay())
            .map(delay -> delay.intValue() / 60)
            .orElse(null));
    departure.setTrack(beRailDeparture.getPlatform());
    departure.setCancelled(
        Optional.ofNullable(beRailDeparture.getCanceled())
            .map(canceled -> canceled == 1)
            .orElse(false));
    departure.setCategory(
        Optional.ofNullable(beRailDeparture.getVehicleinfo())
            .map(
                vehicleInfo -> {
                  String shortName = vehicleInfo.getShortname().split(" ")[0];
                  switch (shortName) {
                    case "IC":
                      return "InterCity";
                    case "L":
                      return "Lokale Trein";
                    default:
                      if (shortName.startsWith("S")) {
                        return "S-trein " + shortName;
                      } else {
                        return shortName;
                      }
                  }
                })
            .orElse(null));
    // TODO: fetch first three stops after current one
    departure.setVia(
        Optional.ofNullable(vehicleResponse.getStops())
            .map(
                stops -> {
                  AtomicBoolean foundCurrentStation = new AtomicBoolean(false);
                  return stops.getStop().stream()
                      .filter(
                          stop -> {
                            if (foundCurrentStation.get()) {
                              return true;
                            }
                            if (stop.getStationinfo()
                                .getStandardname()
                                .equals(station.replace("%20", "-"))) {
                              foundCurrentStation.set(true);
                            }
                            return false;
                          })
                      .skip(1) // Skip the current station
                      .limit(3)
                      .map(stop -> stop.getStationinfo().getStandardname())
                      .collect(Collectors.toList());
                })
            .orElse(List.of()));
    departure.setCrowdForecast(CrowdForecastEnum.UNKNOWN);
    // TODO: seems too high sometimes
    departure.setTrainLength(
        segments.stream()
            .map(segment -> segment.getComposition().getUnits().getNumber())
            .reduce(0, Integer::sum));
    // TODO: seems too high sometimes
    departure.setNumberOfSeats(
        segments.stream()
            .flatMap(segment -> segment.getComposition().getUnits().getUnit().stream())
            .mapToInt(unit -> unit.getSeatsSecondClass() + unit.getSeatsFirstClass())
            .reduce(0, Integer::sum));
    departure.setDepartureStatus(
        Optional.ofNullable(vehicleResponse.getStops())
            .map(
                stops ->
                    stops.getStop().stream()
                        .filter(
                            stop ->
                                stop.getStationinfo()
                                    .getStandardname()
                                    .equals(station.replace("%20", "-")))
                        .findFirst()
                        .map(
                            stop -> {
                              if (stop.getArrived() == 1 && stop.getLeft() == 0) {
                                return DepartureStatusEnum.ON_STATION;
                              } else if (stop.getArrived() == 0) {
                                return DepartureStatusEnum.INCOMING;
                              } else if (stop.getLeft() == 1) {
                                return DepartureStatusEnum.DEPARTED;
                              } else {
                                return DepartureStatusEnum.UNKNOWN;
                              }
                            })
                        .orElse(DepartureStatusEnum.UNKNOWN))
            .orElse(DepartureStatusEnum.UNKNOWN));

    return departure;
  }
}
