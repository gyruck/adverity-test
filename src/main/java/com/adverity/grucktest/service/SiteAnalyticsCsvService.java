package com.adverity.grucktest.service;

import com.adverity.grucktest.helper.SiteAnalyticsCsvLoader;
import com.adverity.grucktest.model.SiteAnalytics;
import com.adverity.grucktest.repository.SiteAnalyticsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for handling CSVs.
 */
@Service
public class SiteAnalyticsCsvService {

  private final SiteAnalyticsRepository siteAnalyticsRepository;
  private final SiteAnalyticsCsvLoader siteAnalyticsCsvLoader;

  @Autowired
  public SiteAnalyticsCsvService(
      SiteAnalyticsRepository siteAnalyticsRepository,
      SiteAnalyticsCsvLoader siteAnalyticsCsvLoader) {
    this.siteAnalyticsRepository = siteAnalyticsRepository;
    this.siteAnalyticsCsvLoader = siteAnalyticsCsvLoader;
  }

  /**
   * Loading and saving SiteAnalytics records from CSV file into database.
   *
   * @param dbUrl Input URL from where downloading the CSV
   */
  public void save(String dbUrl) {
    List<SiteAnalytics> siteAnalyticsList = siteAnalyticsCsvLoader
        .csvToSiteAnalytics(dbUrl);
    siteAnalyticsRepository.saveAll(siteAnalyticsList);
  }
}
