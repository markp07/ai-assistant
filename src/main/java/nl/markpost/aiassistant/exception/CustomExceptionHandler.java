package nl.markpost.aiassistant.exception;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.markpost.aiassistant.api.model.Error;
import nl.markpost.aiassistant.constant.GenericErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomExceptionHandler {

  @ExceptionHandler(GenericException.class)
  public ResponseEntity<Error> handleGenericExceptionException(GenericException exception) {
    log.error("An error occurred", exception);
    return ResponseEntity.internalServerError()
        .body(createError(exception.getErrorCode(), exception.getHttpStatus()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleException(Exception e) {
    log.error("An error occurred", e);
    return ResponseEntity.internalServerError()
        .body(createError(GenericErrorCodes.INTERNAL_SERVER_ERROR));
  }

  private Error createError(GenericErrorCodes errorCode) {
    return createError(errorCode, errorCode.getHttpStatus());
  }

  private Error createError(GenericErrorCodes errorCode, HttpStatus status) {
    var timestamp = Instant.now().atOffset(ZoneOffset.UTC);
    var traceId = UUID.randomUUID().toString();

    return Error.builder()
        .timestamp(timestamp)
        .status(status.value())
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .traceId(traceId)
        .build();
  }
}
