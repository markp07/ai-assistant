package nl.markpost.aiassistant.exception;

import nl.markpost.aiassistant.constant.GenericErrorCodes;

public class NotFoundException extends GenericException {

  public NotFoundException(String message) {
    super(message, GenericErrorCodes.NOT_FOUND);
  }

  public NotFoundException(String message, Exception exception) {
    super(message, GenericErrorCodes.NOT_FOUND, exception);
  }

}
