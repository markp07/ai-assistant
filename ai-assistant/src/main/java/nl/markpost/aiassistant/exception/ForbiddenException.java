package nl.markpost.aiassistant.exception;

import nl.markpost.aiassistant.constant.GenericErrorCodes;

public class ForbiddenException extends GenericException {

  public ForbiddenException(String message) {
    super(message, GenericErrorCodes.FORBIDDEN);
  }

  public ForbiddenException(String message, Exception exception) {
    super(message, GenericErrorCodes.FORBIDDEN, exception);
  }
}
