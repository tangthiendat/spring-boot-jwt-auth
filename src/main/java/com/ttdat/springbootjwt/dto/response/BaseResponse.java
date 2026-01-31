package com.ttdat.springbootjwt.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
  private T data;
  private String message;
  private String exceptionCode;

  protected static final String SUCCESS_REQUEST_MESSAGE = "Success";

  public static <T> BaseResponse<T> of(T data) {
    return BaseResponse.<T>builder()
        .data(data)
        .message(SUCCESS_REQUEST_MESSAGE)
        .exceptionCode(null)
        .build();
  }

  public static <T> BaseResponse<T> of(T data, String message) {
    return BaseResponse.<T>builder().data(data).message(message).exceptionCode(null).build();
  }

  public static <T> BaseResponse<T> ok() {
    return BaseResponse.<T>builder()
        .data(null)
        .message(SUCCESS_REQUEST_MESSAGE)
        .exceptionCode(null)
        .build();
  }

  public static <T> BaseResponse<T> ok(String message) {
    return BaseResponse.<T>builder().data(null).message(message).exceptionCode(null).build();
  }

  public static <T> BaseResponse<T> error(String exceptionCode, String exceptionMessage) {
    return BaseResponse.<T>builder()
        .data(null)
        .message(exceptionMessage)
        .exceptionCode(exceptionCode)
        .build();
  }
}
