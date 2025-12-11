package com.ttdat.springbootjwt.exception;

public class InvalidTokenException extends BaseApplicationException {

  private static final String MESSAGE = "The token is invalid";
  private static final String CODE = "APP.INVALID_JWT_EXCEPTION";

  public InvalidTokenException() {
    super(MESSAGE, CODE);
  }

  public InvalidTokenException(String message) {
    super(message, CODE);
  }

  public InvalidTokenException(String message, String code) {
    super(message, code);
  }
}
