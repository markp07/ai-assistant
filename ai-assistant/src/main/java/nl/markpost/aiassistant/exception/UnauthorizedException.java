package nl.markpost.aiassistant.exception;

import nl.markpost.aiassistant.constant.GenericErrorCodes;

public class UnauthorizedException extends GenericException {

  public UnauthorizedException(String message) {
    super(message, GenericErrorCodes.UNAUTHORIZED);
  }

  public UnauthorizedException(String message, Exception exception) {
    super(message, GenericErrorCodes.UNAUTHORIZED, exception);
  }

}
