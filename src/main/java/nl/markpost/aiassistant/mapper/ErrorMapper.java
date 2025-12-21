package nl.markpost.aiassistant.mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import nl.markpost.aiassistant.api.model.Error;
import nl.markpost.aiassistant.constant.GenericErrorCodes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.HttpStatus;

/** Mapper for converting error codes and HTTP status to Error objects. */
@Mapper(componentModel = "spring")
public interface ErrorMapper {

  /**
   * Maps a GenericErrorCodes and HttpStatus to an Error object.
   *
   * @param errorCode the generic error code
   * @param status the HTTP status
   * @return the mapped Error object
   */
  @Mapping(target = "timestamp", expression = "java(getCurrentTimestamp())")
  @Mapping(target = "status", expression = "java(status.value())")
  @Mapping(target = "code", expression = "java(errorCode.getCode())")
  @Mapping(target = "message", expression = "java(errorCode.getMessage())")
  @Mapping(target = "traceId", expression = "java(generateTraceId())")
  Error from(GenericErrorCodes errorCode, HttpStatus status);

  default OffsetDateTime getCurrentTimestamp() {
    return Instant.now().atOffset(ZoneOffset.UTC);
  }

  default String generateTraceId() {
    return UUID.randomUUID().toString();
  }
}
