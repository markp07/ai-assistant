package nl.markpost.aiassistant.exception;

import nl.markpost.aiassistant.constant.GenericErrorCodes;

public class BadRequestException extends GenericException {

  public BadRequestException(String message) {
    super(message, GenericErrorCodes.BAD_REQUEST);
  }

  public BadRequestException(String message, Exception exception) {
    super(message, GenericErrorCodes.BAD_REQUEST, exception);
  }
}
