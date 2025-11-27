package io.github.masked4j.exception;

public class MaskingException extends RuntimeException {
  public MaskingException(String message) {
    super(message);
  }

  public MaskingException(String message, Throwable cause) {
    super(message, cause);
  }
}
