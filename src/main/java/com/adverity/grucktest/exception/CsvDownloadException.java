package com.adverity.grucktest.exception;

/**
 * Exception for signaling errors during downloading CSV from remote URL.
 */
public class CsvDownloadException extends RuntimeException {

  private static final long serialVersionUID = 3384007401796911768L;

  public CsvDownloadException(String message, Throwable cause) {
    super(message, cause);
  }
}
