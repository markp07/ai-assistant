package nl.markpost.aiassistant.exception;

import nl.markpost.aiassistant.constant.GenericErrorCodes;

public class InternalServerErrorException extends GenericException {

  public InternalServerErrorException(String message) {
    super(message, GenericErrorCodes.INTERNAL_SERVER_ERROR);
  }

  public InternalServerErrorException(String message, Exception exception) {
    super(message, GenericErrorCodes.INTERNAL_SERVER_ERROR, exception);
  }
}
