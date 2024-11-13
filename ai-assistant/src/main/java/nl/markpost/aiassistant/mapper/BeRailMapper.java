package nl.markpost.aiassistant.mapper;

import jakarta.validation.Valid;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import nl.markpost.aiassistant.api.model.Departure;
import nl.markpost.aiassistant.api.model.Departure.CrowdForecastEnum;
import nl.markpost.aiassistant.api.model.DepartureStatusEnum;
import nl.markpost.aiassistant.external.api.berail.model.CompositionResponse;
import nl.markpost.aiassistant.external.api.berail.model.CompositionResponseCompositionSegmentsSegmentInner;
import nl.markpost.aiassistant.external.api.berail.model.LiveboardResponseDeparturesDepartureInner;
import nl.markpost.aiassistant.external.api.berail.model.VehicleResponse;
import org.springframework.stereotype.Service;

@Service
public class BeRailMapper {

  public Departure mapDeparture(String station, LiveboardResponseDeparturesDepartureInner beRailDeparture,
      VehicleResponse vehicleResponse, CompositionResponse compositionResponse) {
    Departure departure = new Departure();

    List<@Valid CompositionResponseCompositionSegmentsSegmentInner> segments = compositionResponse.getComposition()
        .getSegments().getSegment();

    departure.setTrainNumber(beRailDeparture.getVehicleinfo().getShortname().split(" ")[1]);
    departure.setDestination(beRailDeparture.getStationinfo().getStandardname());
    departure.setDepartureTime(
        OffsetDateTime.ofInstant(Instant.ofEpochSecond(beRailDeparture.getTime().longValue()),
            ZoneOffset.ofHours(1)));
    departure.setDelay(beRailDeparture.getDelay().intValue() / 60);
    departure.setTrack(beRailDeparture.getPlatform());
    departure.setCancelled(beRailDeparture.getCanceled() == 1);
    departure.setCategory(beRailDeparture.getVehicleinfo().getShortname().split(" ")[0]);
    departure.setVia(vehicleResponse.getStops().getStop().stream().limit(3)
        .map(stop -> stop.getStationinfo().getStandardname()).collect(Collectors.toList()));
    departure.setCrowdForecast(CrowdForecastEnum.UNKNOWN);
    departure.setTrainLength(
        segments.stream().map(segment -> segment.getComposition().getUnits().getNumber())
            .reduce(0, Integer::sum));
    departure.setNumberOfSeats(segments.stream()
        .flatMap(segment -> segment.getComposition().getUnits().getUnit().stream())
        .mapToInt(unit -> unit.getSeatsSecondClass() + unit.getSeatsFirstClass())
        .reduce(0, Integer::sum));
    departure.setDepartureStatus(vehicleResponse.getStops().getStop().stream()
        .filter(stop -> stop.getStationinfo().getStandardname().equals(station))
        .findFirst()
        .map(stop -> {
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
        .orElse(DepartureStatusEnum.UNKNOWN));

    return departure;
  }

}
