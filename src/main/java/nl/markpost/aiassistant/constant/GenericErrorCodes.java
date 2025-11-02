package nl.markpost.aiassistant.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GenericErrorCodes {
  BAD_REQUEST("BAD_REQUEST", "Bad request", HttpStatus.BAD_REQUEST),
  NOT_FOUND("NOT_FOUND", "Not found", HttpStatus.NOT_FOUND),
  FORBIDDEN("FORBIDDEN", "Forbidden", HttpStatus.FORBIDDEN),
  UNAUTHORIZED("UNAUTHORIZED", "Unauthorized", HttpStatus.UNAUTHORIZED),
  INTERNAL_SERVER_ERROR(
      "INTERNAL_SERVER_ERROR", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
  SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;
}
