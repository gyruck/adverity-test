package com.adverity.grucktest.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adverity.grucktest.model.SiteAnalytics;
import com.adverity.grucktest.model.SiteAnalyticsCriteria;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Testing SiteAnalytics repository (saving, querying).
 */
@DataJpaTest
public class SiteAnalyticsRepositoryTests {

  private static final String DATASOURCE = "Twitter Ads";
  private static final String CAMPAIGN = "DE|SN|Snow Bindungen|Brands";
  private static final String FROM = "01/29/19";
  private static final String TO = "01/30/19";

  private static final SiteAnalyticsCriteria EMPTY_CRITERIA = new SiteAnalyticsCriteria();
  private static final SiteAnalyticsCriteria CRITERIA_WITH_DATASOURCE = SiteAnalyticsCriteria
      .builder()
      .dataSource(DATASOURCE)
      .build();
  private static final SiteAnalyticsCriteria CRITERIA_WITH_DATASOURCE_AND_CAMPAIGN = SiteAnalyticsCriteria
      .builder()
      .dataSource(DATASOURCE)
      .campaign(CAMPAIGN)
      .build();
  private static final SiteAnalyticsCriteria CRITERIA_WITH_FROM_AND_TO = SiteAnalyticsCriteria
      .builder()
      .from(LocalDate.parse(FROM, DateTimeFormatter
          .ofPattern(SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT)))
      .to(LocalDate.parse(TO, DateTimeFormatter
          .ofPattern(SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT)))
      .build();
  private static final SiteAnalyticsCriteria CRITERIA_WITH_INVALID_DATASOURCE = SiteAnalyticsCriteria
      .builder()
      .dataSource("not available datasource, sorry m8")
      .build();

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SiteAnalyticsRepository siteAnalyticsRepository;

  private List<SiteAnalytics> siteAnalyticsList;

  @BeforeEach
  public void initDb() {
    givenSiteAnalyticsRecords();
  }

  @Test
  public void testFindSiteAnalytics_whenNoCriteriaIsGiven() {
    List<SiteAnalytics> siteAnalyticsList = whenFindSiteAnalyticsIsCalledWithoutParam();
    thenAllRecordsShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testFindSiteAnalytics_whenDataSourceIsGiven() {
    List<SiteAnalytics> siteAnalyticsList = whenFindSiteAnalyticsIsCalledWithDataSource();
    thenThreeRecordsShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testFindSiteAnalytics_whenDataSourceAndCampaignIsGiven() {
    List<SiteAnalytics> siteAnalyticsList = whenFindSiteAnalyticsIsCalledWithDataSourceAndCampaign();
    thenTwoRecordsShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testFindSiteAnalytics_whenFromAndToIsGiven() {
    List<SiteAnalytics> siteAnalyticsList = whenFindSiteAnalyticsIsCalledWithFromAndTo();
    thenTwoRecordsShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testFindSiteAnalytics_whenCriteriaProvidesNoResult() {
    List<SiteAnalytics> siteAnalyticsList = whenCriteriaHasNoResult();
    thenNoRecordsShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testFindSumOfSiteAnalyticsStats_whenDatasourceIsNotSet() {
    List<SiteAnalytics> siteAnalyticsList = whenFindSumOfSiteAnalyticsStatIsCalledWithoutParam();
    thenThreeRecordsShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testFindSumOfSiteAnalyticsStats_whenDatasourceIsSet() {
    List<SiteAnalytics> siteAnalyticsList = whenFindSumOfSiteAnalyticsStatIsCalledWithDatasource();
    thenTwoRecordsShouldBeReturned(siteAnalyticsList);
  }

  @Test
  public void testFindSumOfSiteAnalyticsStats_whenDatasourceAndCampaignIsSet() {
    List<SiteAnalytics> siteAnalyticsList = whenFindSumOfSiteAnalyticsStatIsCalledWithDatasourceAndCampaign();
    thenOneRecordsShouldBeReturned(siteAnalyticsList);
  }

  private void givenSiteAnalyticsRecords() {
    siteAnalyticsList = new ArrayList<>();

    siteAnalyticsList.add(
        new SiteAnalytics("Google Ads", "Adventmarkt Touristik", "11/12/19", 7,
            22425));
    siteAnalyticsList.add(
        new SiteAnalytics(DATASOURCE, "DE | SP Gadgets", "01/04/19", 81,
            1317));
    siteAnalyticsList.add(
        new SiteAnalytics(DATASOURCE, CAMPAIGN, FROM, 1,
            42));
    siteAnalyticsList.add(
        new SiteAnalytics(DATASOURCE, CAMPAIGN, TO, 1,
            40));

    for (SiteAnalytics siteAnalytics : siteAnalyticsList) {
      entityManager.persist(siteAnalytics);
    }
    entityManager.flush();
  }

  private List<SiteAnalytics> whenFindSiteAnalyticsIsCalledWithoutParam() {
    return siteAnalyticsRepository.findSiteAnalytics(EMPTY_CRITERIA);
  }

  private List<SiteAnalytics> whenFindSiteAnalyticsIsCalledWithDataSource() {
    return siteAnalyticsRepository.findSiteAnalytics(CRITERIA_WITH_DATASOURCE);
  }

  private List<SiteAnalytics> whenFindSiteAnalyticsIsCalledWithDataSourceAndCampaign() {
    return siteAnalyticsRepository
        .findSiteAnalytics(CRITERIA_WITH_DATASOURCE_AND_CAMPAIGN);
  }

  private List<SiteAnalytics> whenFindSiteAnalyticsIsCalledWithFromAndTo() {
    return siteAnalyticsRepository.findSiteAnalytics(CRITERIA_WITH_FROM_AND_TO);
  }

  private List<SiteAnalytics> whenCriteriaHasNoResult() {
    return siteAnalyticsRepository
        .findSiteAnalytics(CRITERIA_WITH_INVALID_DATASOURCE);
  }

  private List<SiteAnalytics> whenFindSumOfSiteAnalyticsStatIsCalledWithoutParam() {
    return siteAnalyticsRepository
        .findSumOfSiteAnalyticsStats(new SiteAnalyticsCriteria());
  }

  private List<SiteAnalytics> whenFindSumOfSiteAnalyticsStatIsCalledWithDatasource() {
    return siteAnalyticsRepository
        .findSumOfSiteAnalyticsStats(CRITERIA_WITH_DATASOURCE);
  }

  private List<SiteAnalytics> whenFindSumOfSiteAnalyticsStatIsCalledWithDatasourceAndCampaign() {
    return siteAnalyticsRepository
        .findSumOfSiteAnalyticsStats(CRITERIA_WITH_DATASOURCE_AND_CAMPAIGN);
  }


  private void thenAllRecordsShouldBeReturned(
      List<SiteAnalytics> siteAnalyticsList) {
    assertEquals(this.siteAnalyticsList.size(), siteAnalyticsList.size());
  }

  private void thenThreeRecordsShouldBeReturned(
      List<SiteAnalytics> siteAnalyticsList) {
    assertEquals(3, siteAnalyticsList.size());
  }

  private void thenTwoRecordsShouldBeReturned(
      List<SiteAnalytics> siteAnalyticsList) {
    assertEquals(2, siteAnalyticsList.size());
  }

  private void thenOneRecordsShouldBeReturned(
      List<SiteAnalytics> siteAnalyticsList) {
    assertEquals(1, siteAnalyticsList.size());
  }

  private void thenNoRecordsShouldBeReturned(
      List<SiteAnalytics> siteAnalyticsList) {
    assertEquals(0, siteAnalyticsList.size());
  }
}
