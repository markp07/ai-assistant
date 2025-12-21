package nl.markpost.aiassistant.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.markpost.aiassistant.api.model.Error;
import nl.markpost.aiassistant.constant.GenericErrorCodes;
import nl.markpost.aiassistant.mapper.ErrorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandler {

  private final ErrorMapper errorMapper;

  /**
   * Handles GenericException and maps it to an appropriate HTTP response.
   *
   * @param exception The GenericException to handle.
   * @return A ResponseEntity containing the error details.
   */
  @org.springframework.web.bind.annotation.ExceptionHandler(GenericException.class)
  public ResponseEntity<Error> handleGenericExceptionException(GenericException exception) {
    log.error("An error occurred", exception);
    return ResponseEntity.internalServerError()
        .body(errorMapper.from(exception.getErrorCode(), exception.getHttpStatus()));
  }

  /**
   * Handles all other exceptions and maps them to a generic internal server error response.
   *
   * @param e The exception to handle.
   * @return A ResponseEntity containing the error details.
   */
  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleException(Exception e) {
    log.error("An error occurred", e);
    return ResponseEntity.internalServerError()
        .body(errorMapper.from(GenericErrorCodes.INTERNAL_SERVER_ERROR,
            GenericErrorCodes.INTERNAL_SERVER_ERROR.getHttpStatus()));
  }
}
