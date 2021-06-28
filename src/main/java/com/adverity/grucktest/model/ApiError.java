package com.adverity.grucktest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Error class for providing error responses in the API.
 */
@Data
@AllArgsConstructor
public class ApiError {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp = LocalDateTime.now();
  private String message;
  private String error;

  public ApiError(Throwable ex) {
    this.message = "Error happened";
    this.error = ex.getLocalizedMessage();
  }

  public ApiError(String message, Throwable ex) {
    this.message = message;
    this.error = ex.getLocalizedMessage();
  }
}
