package com.adverity.grucktest.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.adverity.grucktest.exception.CsvParseException;
import com.adverity.grucktest.model.SiteAnalytics;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testing loading CSV and transforming it into SiteAnalytics entities.
 */
public class SiteAnalyticsCsvLoaderTests {

  private static final String CSV_HEADER = "Datasource,Campaign,Daily,Clicks,Impressions\n";
  private static final String VALID_CSV =
      CSV_HEADER + "Google Ads,Adventmarkt Touristik,11/12/19,7,22425";
  private static final String CSV_WITH_INVALID_DATE =
      CSV_HEADER + "Google Ads,Adventmarkt Touristik,I'm not a date,7,22425";
  private static final String CSV_WITH_INVALID_NUMBER = CSV_HEADER
      + "Google Ads,Adventmarkt Touristik,11/12/19,you can't count me man,22425";
  private static final String CSV_WITH_LESS_COLUMNS =
      CSV_HEADER + "Google Ads,do you want more?";

  private static final String DATASOURCE = "Google Ads";
  private static final String CAMPAIGN = "Adventmarkt Touristik";
  private static final String DATE = "11/12/19";
  private static final String CLICKS = "7";
  private static final String IMPRESSIONS = "22425";

  private SiteAnalyticsCsvLoader siteAnalyticsCsvLoader;
  private InputStream csvInputStream;

  @BeforeEach
  public void init() {
    givenDependencies();
  }

  @Test
  public void testCsvToGoogleAnalyticsMetrics_whenValidCsvIsGiven_thenItShouldReturnValidRecords() {
    givenValidCsv();

    List<SiteAnalytics> siteAnalyticsList = whenCsvToGoogleAnalyticsMetricsIsCalled();

    thenOneSiteAnalyticsRecordShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testCsvLoader_whenCsvIsLoadedWithInvalidDate_thenItShouldFail() {
    givenCsvWithInvalidDate();

    thenCsvParseExceptionShouldBeThrown();
  }

  @Test
  public void testCsvLoader_whenCsvIsLoadedWithInvalidNumber_thenItShouldFail() {
    givenCsvWithInvalidNumber();

    thenCsvParseExceptionShouldBeThrown();
  }

  @Test
  public void testCsvLoader_whenCsvIsLoadedWithLessColumns_thenItShouldFail() {
    givenCsvWithLessColumns();

    thenCsvParseExceptionShouldBeThrown();
  }

  private void givenDependencies() {
    this.siteAnalyticsCsvLoader = new SiteAnalyticsCsvLoader();
  }

  private void givenValidCsv() {
    csvInputStream = new ByteArrayInputStream(VALID_CSV.getBytes(
        StandardCharsets.UTF_8));
  }

  private void givenCsvWithInvalidDate() {
    csvInputStream = new ByteArrayInputStream(CSV_WITH_INVALID_DATE.getBytes(
        StandardCharsets.UTF_8));
  }

  private void givenCsvWithInvalidNumber() {
    csvInputStream = new ByteArrayInputStream(CSV_WITH_INVALID_NUMBER.getBytes(
        StandardCharsets.UTF_8));
  }

  private void givenCsvWithLessColumns() {
    csvInputStream = new ByteArrayInputStream(CSV_WITH_LESS_COLUMNS.getBytes(
        StandardCharsets.UTF_8));
  }

  private List<SiteAnalytics> whenCsvToGoogleAnalyticsMetricsIsCalled() {
    return siteAnalyticsCsvLoader.csvToSiteAnalytics(csvInputStream);
  }

  private void thenOneSiteAnalyticsRecordShouldBeReturned(
      List<SiteAnalytics> siteAnalyticsList) {
    assertEquals(1, siteAnalyticsList.size());
    assertEquals(DATASOURCE, siteAnalyticsList.get(0).getDatasource());
    assertEquals(CAMPAIGN, siteAnalyticsList.get(0).getCampaign());
    assertEquals(LocalDate.parse(DATE, DateTimeFormatter
            .ofPattern(SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT)),
        siteAnalyticsList.get(0).getDaily());
    assertEquals(Long.parseLong(CLICKS), siteAnalyticsList.get(0).getClicks());
    assertEquals(Long.parseLong(IMPRESSIONS),
        siteAnalyticsList.get(0).getImpressions());
  }

  private void thenCsvParseExceptionShouldBeThrown() {
    assertThrows(CsvParseException.class,
        this::whenCsvToGoogleAnalyticsMetricsIsCalled);
  }
}
