package nl.markpost.aiassistant.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import nl.markpost.aiassistant.api.model.Error;
import nl.markpost.aiassistant.constant.GenericErrorCodes;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

class ErrorMapperTest {

  private final ErrorMapper errorMapper = Mappers.getMapper(ErrorMapper.class);

  @Test
  void from_shouldMapGenericErrorCodeToError() {
    GenericErrorCodes errorCode = GenericErrorCodes.INTERNAL_SERVER_ERROR;
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    Error result = errorMapper.from(errorCode, status);

    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(500);
    assertThat(result.getCode()).isEqualTo(errorCode.getCode());
    assertThat(result.getMessage()).isEqualTo(errorCode.getMessage());
    assertThat(result.getTraceId()).isNotNull();
    assertThat(result.getTimestamp()).isNotNull();
  }

  @Test
  void from_shouldMapBadRequestError() {
    GenericErrorCodes errorCode = GenericErrorCodes.BAD_REQUEST;
    HttpStatus status = HttpStatus.BAD_REQUEST;

    Error result = errorMapper.from(errorCode, status);

    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(400);
    assertThat(result.getCode()).isEqualTo(errorCode.getCode());
    assertThat(result.getMessage()).isEqualTo(errorCode.getMessage());
  }

  @Test
  void from_shouldMapUnauthorizedError() {
    GenericErrorCodes errorCode = GenericErrorCodes.UNAUTHORIZED;
    HttpStatus status = HttpStatus.UNAUTHORIZED;

    Error result = errorMapper.from(errorCode, status);

    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(401);
    assertThat(result.getCode()).isEqualTo(errorCode.getCode());
    assertThat(result.getMessage()).isEqualTo(errorCode.getMessage());
  }

  @Test
  void getCurrentTimestamp_shouldReturnCurrentTimestamp() {
    OffsetDateTime before = Instant.now().atOffset(ZoneOffset.UTC);

    OffsetDateTime result = errorMapper.getCurrentTimestamp();

    OffsetDateTime after = Instant.now().atOffset(ZoneOffset.UTC);
    assertThat(result).isNotNull();
    assertThat(result).isBetween(before, after);
  }

  @Test
  void generateTraceId_shouldReturnValidUUID() {
    String traceId = errorMapper.generateTraceId();

    assertThat(traceId).isNotNull();
    assertThat(traceId).matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
  }

  @Test
  void generateTraceId_shouldReturnUniqueValues() {
    String traceId1 = errorMapper.generateTraceId();
    String traceId2 = errorMapper.generateTraceId();

    assertThat(traceId1).isNotEqualTo(traceId2);
  }
}
