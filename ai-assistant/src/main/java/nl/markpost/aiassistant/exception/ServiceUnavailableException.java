package nl.markpost.aiassistant.exception;

import nl.markpost.aiassistant.constant.GenericErrorCodes;

public class ServiceUnavailableException extends GenericException {

  public ServiceUnavailableException(String message) {
    super(message, GenericErrorCodes.SERVICE_UNAVAILABLE);
  }

  public ServiceUnavailableException(String message, Exception exception) {
    super(message, GenericErrorCodes.SERVICE_UNAVAILABLE, exception);
  }

}
