package com.ttdat.springbootjwt.exception;

import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public abstract class BaseApplicationException extends Exception {
  private final @NonNull String code;

  public BaseApplicationException(@NonNull final String message, @NonNull final String code) {
    super(message);
    this.code = code;
  }
}
