package com.adverity.grucktest.service;

import com.adverity.grucktest.helper.SiteAnalyticsCsvLoader;
import com.adverity.grucktest.model.SiteAnalytics;
import com.adverity.grucktest.repository.SiteAnalyticsRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Testing CSV service.
 */
@DataJpaTest
public class SiteAnalyticsCsvServiceTests {

  @MockBean
  SiteAnalyticsCsvLoader siteAnalyticsCsvLoader;

  @Autowired
  SiteAnalyticsRepository siteAnalyticsRepository;

  SiteAnalyticsCsvService siteAnalyticsCsvService;

  @BeforeEach
  public void init() {
    siteAnalyticsCsvService = new SiteAnalyticsCsvService(
        siteAnalyticsRepository, siteAnalyticsCsvLoader);
  }

  @Test
  public void test() {
    List<SiteAnalytics> siteAnalyticsList = givenSiteAnalyticsRecords();
    whenCsvIsParsed(siteAnalyticsList);
    whenSaveIsCalled();
    thenRepositoryShouldContainThatManyElements(siteAnalyticsList);
  }

  private List<SiteAnalytics> givenSiteAnalyticsRecords() {
    List<SiteAnalytics> siteAnalyticsList = new ArrayList<>();

    siteAnalyticsList.add(
        new SiteAnalytics("Google Ads", "Adventmarkt Touristik", "11/12/19", 7,
            22425));
    siteAnalyticsList.add(
        new SiteAnalytics("Twitter Ads", "DE | SP Gadgets", "01/04/19", 81,
            1317));

    return siteAnalyticsList;
  }

  private void whenCsvIsParsed(List<SiteAnalytics> siteAnalyticsList) {
    Mockito.when(siteAnalyticsCsvLoader.csvToSiteAnalytics(Mockito.anyString()))
        .thenReturn(siteAnalyticsList);
  }

  private void whenSaveIsCalled() {
    siteAnalyticsCsvService.save("url");
  }

  private void thenRepositoryShouldContainThatManyElements(
      List<SiteAnalytics> siteAnalyticsList) {
    Assertions.assertEquals(siteAnalyticsList.size(),
        siteAnalyticsRepository.findAll().size());
  }
}
