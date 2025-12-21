package nl.markpost.aiassistant.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.markpost.aiassistant.api.model.Error;
import nl.markpost.aiassistant.constant.GenericErrorCodes;
import nl.markpost.aiassistant.mapper.ErrorMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTest {

  @Mock private ErrorMapper errorMapper;

  @InjectMocks private ExceptionHandler exceptionHandler;

  @Test
  void handleGenericExceptionException_shouldReturnInternalServerError() {
    GenericException exception =
        new GenericException(
            GenericErrorCodes.INTERNAL_SERVER_ERROR.getMessage(),
            GenericErrorCodes.INTERNAL_SERVER_ERROR);

    Error error = new Error();
    error.setStatus(500);
    error.setCode(GenericErrorCodes.INTERNAL_SERVER_ERROR.getCode());
    error.setMessage(GenericErrorCodes.INTERNAL_SERVER_ERROR.getMessage());

    when(errorMapper.from(
            GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR))
        .thenReturn(error);

    ResponseEntity<Error> response = exceptionHandler.handleGenericExceptionException(exception);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getStatus()).isEqualTo(500);
    assertThat(response.getBody().getCode())
        .isEqualTo(GenericErrorCodes.INTERNAL_SERVER_ERROR.getCode());
    verify(errorMapper)
        .from(GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void handleGenericExceptionException_shouldReturnBadRequest() {
    GenericException exception =
        new GenericException(
            GenericErrorCodes.BAD_REQUEST.getMessage(), GenericErrorCodes.BAD_REQUEST);

    Error error = new Error();
    error.setStatus(400);
    error.setCode(GenericErrorCodes.BAD_REQUEST.getCode());
    error.setMessage(GenericErrorCodes.BAD_REQUEST.getMessage());

    when(errorMapper.from(GenericErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST)).thenReturn(error);

    ResponseEntity<Error> response = exceptionHandler.handleGenericExceptionException(exception);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getStatus()).isEqualTo(400);
    verify(errorMapper).from(GenericErrorCodes.BAD_REQUEST, HttpStatus.BAD_REQUEST);
  }

  @Test
  void handleGenericExceptionException_shouldReturnUnauthorized() {
    GenericException exception =
        new GenericException(
            GenericErrorCodes.UNAUTHORIZED.getMessage(), GenericErrorCodes.UNAUTHORIZED);

    Error error = new Error();
    error.setStatus(401);
    error.setCode(GenericErrorCodes.UNAUTHORIZED.getCode());
    error.setMessage(GenericErrorCodes.UNAUTHORIZED.getMessage());

    when(errorMapper.from(GenericErrorCodes.UNAUTHORIZED, HttpStatus.UNAUTHORIZED))
        .thenReturn(error);

    ResponseEntity<Error> response = exceptionHandler.handleGenericExceptionException(exception);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getStatus()).isEqualTo(401);
    verify(errorMapper).from(GenericErrorCodes.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
  }

  @Test
  void handleException_shouldReturnInternalServerErrorForGenericException() {
    Exception exception = new RuntimeException("Something went wrong");

    Error error = new Error();
    error.setStatus(500);
    error.setCode(GenericErrorCodes.INTERNAL_SERVER_ERROR.getCode());
    error.setMessage(GenericErrorCodes.INTERNAL_SERVER_ERROR.getMessage());

    when(errorMapper.from(
            GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR))
        .thenReturn(error);

    ResponseEntity<Error> response = exceptionHandler.handleException(exception);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getStatus()).isEqualTo(500);
    assertThat(response.getBody().getCode())
        .isEqualTo(GenericErrorCodes.INTERNAL_SERVER_ERROR.getCode());
    verify(errorMapper)
        .from(GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void handleException_shouldHandleNullPointerException() {
    Exception exception = new NullPointerException("Null value encountered");

    Error error = new Error();
    error.setStatus(500);
    error.setCode(GenericErrorCodes.INTERNAL_SERVER_ERROR.getCode());
    error.setMessage(GenericErrorCodes.INTERNAL_SERVER_ERROR.getMessage());

    when(errorMapper.from(
            GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR))
        .thenReturn(error);

    ResponseEntity<Error> response = exceptionHandler.handleException(exception);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
    verify(errorMapper)
        .from(GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void handleException_shouldHandleIllegalArgumentException() {
    Exception exception = new IllegalArgumentException("Invalid argument");

    Error error = new Error();
    error.setStatus(500);
    error.setCode(GenericErrorCodes.INTERNAL_SERVER_ERROR.getCode());
    error.setMessage(GenericErrorCodes.INTERNAL_SERVER_ERROR.getMessage());

    when(errorMapper.from(
            GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR))
        .thenReturn(error);

    ResponseEntity<Error> response = exceptionHandler.handleException(exception);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    verify(errorMapper)
        .from(GenericErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
