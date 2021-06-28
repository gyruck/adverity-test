package com.adverity.grucktest.helper;

import com.adverity.grucktest.exception.CsvDownloadException;
import com.adverity.grucktest.exception.CsvParseException;
import com.adverity.grucktest.model.SiteAnalytics;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

/**
 * Loading and transforming Site Analytics CSV.
 */
@Component
public class SiteAnalyticsCsvLoader {

  /**
   * Loading csv from URL and transforming it into a list of entities.
   *
   * @param url Input URL of the CSV file
   * @return List of SiteAnalytics records
   */
  public List<SiteAnalytics> csvToSiteAnalytics(
      String url) {
    try (BufferedInputStream csv = new BufferedInputStream(
        new URL(url).openStream())) {

      return csvToSiteAnalytics(csv);
    } catch (IOException e) {
      throw new CsvDownloadException("Failed to open CSV file", e);
    }
  }

  /**
   * Loading csv from InputStream and transforming it into a list of entities.
   *
   * @param csvInputStream InputStream from which we are loading csv file
   * @return List of SiteAnalytics records
   */
  public List<SiteAnalytics> csvToSiteAnalytics(
      InputStream csvInputStream) {

    try (CSVParser csvParser = createCsvParser(csvInputStream)) {
      List<SiteAnalytics> siteAnalyticsMetrics = new ArrayList<>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
        SiteAnalytics siteAnalyticsRecord = new SiteAnalytics(
            csvRecord.get(SiteAnalyticsCsvHeaders.DATASOURCE),
            csvRecord.get(SiteAnalyticsCsvHeaders.CAMPAIGN),
            csvRecord.get(SiteAnalyticsCsvHeaders.DAILY),
            Long.parseLong(csvRecord.get(SiteAnalyticsCsvHeaders.CLICKS)),
            Long.parseLong(csvRecord.get(SiteAnalyticsCsvHeaders.IMPRESSIONS))
        );

        siteAnalyticsMetrics.add(siteAnalyticsRecord);
      }

      return siteAnalyticsMetrics;
    } catch (IllegalArgumentException ex) {
      throw new CsvParseException(
          "Input CSV format seems to be missing columns or has invalid format",
          ex);
    } catch (DateTimeParseException ex) {
      throw new CsvParseException(
          "Input CSV has invalid date format, valid format is: "
              + SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT, ex);
    } catch (IOException ex) {
      throw new CsvParseException(
          "Cannot parse input csv", ex);
    }
  }

  private CSVParser createCsvParser(InputStream is) throws IOException {
    BufferedReader csvReader = new BufferedReader(
        new InputStreamReader(is,
            StandardCharsets.UTF_8));

    return new CSVParser(csvReader,
        CSVFormat.DEFAULT
            .withHeader(SiteAnalyticsCsvHeaders.class)
            .withIgnoreHeaderCase()
            .withSkipHeaderRecord()
            .withTrim());
  }
}
