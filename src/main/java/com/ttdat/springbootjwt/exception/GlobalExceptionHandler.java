package com.ttdat.springbootjwt.exception;

import com.ttdat.springbootjwt.dto.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String VALIDATION_VIOLATION_EXCEPTION_CODE =
      "APP.VALIDATION_VIOLATION_EXCEPTION";
  private static final String MESSAGE_NOT_READABLE_EXCEPTION_CODE =
      "APP.MESSAGE_NOT_READABLE_EXCEPTION";
  private static final String CONSTRAINT_VIOLATION_EXCEPTION_CODE =
      "APP.CONSTRAINT_VIOLATION_EXCEPTION";

  private static final String ACCESS_DENIED_EXCEPTION_CODE = "APP.ACCESS_DENIED_EXCEPTION";
  private static final String ACCESS_DENIED_EXCEPTION_MESSAGE =
      "You do not have permission to access this resource";

  private static final String BAD_CREDENTIAL_EXCEPTION_CODE = "APP.BAD_CREDENTIAL_EXCEPTION";
  private static final String BAD_CREDENTIAL_EXCEPTION_MESSAGE =
      "The email or password is wrong, please try again";

  private static final String DISABLED_ACCOUNT_EXCEPTION_CODE = "APP.DISABLED_ACCOUNT_EXCEPTION";
  private static final String DISABLED_ACCOUNT_EXCEPTION_MESSAGE =
      "The account is disabled, please contact administrator";

  private ResponseEntity<Object> wrapWithResponse(String code, String message, HttpStatus status) {
    return new ResponseEntity<>(BaseResponse.error(code, message), status);
  }

  @ExceptionHandler(BaseApplicationException.class)
  public ResponseEntity<Object> handleBaseApplicationException(BaseApplicationException exception) {
    return wrapWithResponse(exception.getCode(), exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
    return wrapWithResponse(
        BAD_CREDENTIAL_EXCEPTION_CODE, BAD_CREDENTIAL_EXCEPTION_MESSAGE, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<Object> handleDisabledException(DisabledException exception) {
    return wrapWithResponse(
        DISABLED_ACCOUNT_EXCEPTION_CODE,
        DISABLED_ACCOUNT_EXCEPTION_MESSAGE,
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
    var rootCause = ExceptionUtils.getRootCause(exception);
    var isInstanceOfBaseApplicationException = rootCause instanceof BaseApplicationException;
    var isInstanceOfBadCredentialsException = rootCause instanceof BadCredentialsException;

    if (isInstanceOfBaseApplicationException)
      return this.handleBaseApplicationException((BaseApplicationException) exception.getCause());
    else if (isInstanceOfBadCredentialsException)
      return this.handleBadCredentialsException((BadCredentialsException) exception.getCause());

    return wrapWithResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.name(),
        exception.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException exception) {
    return wrapWithResponse(
        CONSTRAINT_VIOLATION_EXCEPTION_CODE, exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    return wrapWithResponse(
        ACCESS_DENIED_EXCEPTION_CODE, ACCESS_DENIED_EXCEPTION_MESSAGE, HttpStatus.FORBIDDEN);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    return wrapWithResponse(
        VALIDATION_VIOLATION_EXCEPTION_CODE,
        exception.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", ")),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      @NonNull HttpMessageNotReadableException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    return wrapWithResponse(
        MESSAGE_NOT_READABLE_EXCEPTION_CODE, exception.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
