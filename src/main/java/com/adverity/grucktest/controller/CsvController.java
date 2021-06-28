package com.adverity.grucktest.controller;

import com.adverity.grucktest.exception.CsvDownloadException;
import com.adverity.grucktest.model.ApiError;
import com.adverity.grucktest.service.SiteAnalyticsCsvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling CSV input database.
 */
@RestController
@RequestMapping("/database")
@Slf4j
public class CsvController {

  private static final String CSV_URL = "http://adverity-challenge.s3-website-eu-west-1.amazonaws.com/PIxSyyrIKFORrCXfMYqZBI.csv";

  private final SiteAnalyticsCsvService siteAnalyticsCsvService;

  @Autowired
  public CsvController(
      SiteAnalyticsCsvService siteAnalyticsCsvService) {
    this.siteAnalyticsCsvService = siteAnalyticsCsvService;
  }

  /**
   * Loading database from S3.
   */
  @GetMapping(path = "/load-from-s3")
  @ResponseBody
  public void load() {
    siteAnalyticsCsvService.save(CSV_URL);
  }

  /**
   * Exception handler for downloading and parsing the CSV.
   *
   * @param ex Handling CsvDownloadException errors
   * @return Error response in JSON format
   */
  @ExceptionHandler(CsvDownloadException.class)
  public ResponseEntity<ApiError> csvDownloadException(
      CsvDownloadException ex) {
    ApiError response = new ApiError(ex);

    return new ResponseEntity<ApiError>(response, HttpStatus.BAD_REQUEST);
  }
}
