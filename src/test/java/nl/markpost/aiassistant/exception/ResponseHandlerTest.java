package nl.markpost.aiassistant.exception;

import static org.assertj.core.api.Assertions.assertThat;

import feign.Request;
import feign.Response;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponseHandlerTest {

  private ResponseHandler responseHandler;

  @BeforeEach
  void setUp() {
    responseHandler = new ResponseHandler();
  }

  @Test
  void decode_shouldReturnBadRequestExceptionFor400() {
    Response response = createMockResponse(400, "Bad Request");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(BadRequestException.class);
    assertThat(result.getMessage()).isEqualTo("Bad Request");
  }

  @Test
  void decode_shouldReturnUnauthorizedExceptionFor401() {
    Response response = createMockResponse(401, "Unauthorized");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(UnauthorizedException.class);
    assertThat(result.getMessage()).isEqualTo("Unauthorized");
  }

  @Test
  void decode_shouldReturnForbiddenExceptionFor403() {
    Response response = createMockResponse(403, "Forbidden");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(ForbiddenException.class);
    assertThat(result.getMessage()).isEqualTo("Forbidden");
  }

  @Test
  void decode_shouldReturnNotFoundExceptionFor404() {
    Response response = createMockResponse(404, "Not Found");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(NotFoundException.class);
    assertThat(result.getMessage()).isEqualTo("Not Found");
  }

  @Test
  void decode_shouldReturnInternalServerErrorExceptionFor500() {
    Response response = createMockResponse(500, "Internal Server Error");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(InternalServerErrorException.class);
    assertThat(result.getMessage()).isEqualTo("Internal Server Error");
  }

  @Test
  void decode_shouldReturnServiceUnavailableExceptionFor503() {
    Response response = createMockResponse(503, "Service Unavailable");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(ServiceUnavailableException.class);
    assertThat(result.getMessage()).isEqualTo("Service Unavailable");
  }

  @Test
  void decode_shouldReturnGenericExceptionForUnknownStatusCode() {
    Response response = createMockResponse(418, "I'm a teapot");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(GenericException.class);
    assertThat(result.getMessage()).isEqualTo("Generic Error");
  }

  @Test
  void decode_shouldReturnGenericExceptionFor502() {
    Response response = createMockResponse(502, "Bad Gateway");

    Exception result = responseHandler.decode("methodKey", response);

    assertThat(result).isInstanceOf(GenericException.class);
    assertThat(result.getMessage()).isEqualTo("Generic Error");
  }

  private Response createMockResponse(int status, String reason) {
    Request request =
        Request.create(Request.HttpMethod.GET, "/test", new HashMap<>(), null, null, null);
    return Response.builder()
        .status(status)
        .reason(reason)
        .request(request)
        .headers(new HashMap<>())
        .build();
  }
}
