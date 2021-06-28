package com.adverity.grucktest.exception;

/**
 * Exception for signaling parsing errors during loading CSVs.
 */
public class CsvParseException extends RuntimeException {

  private static final long serialVersionUID = -8883157767322775190L;

  public CsvParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
