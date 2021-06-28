package com.adverity.grucktest.service;

import com.adverity.grucktest.model.SiteAnalytics;
import com.adverity.grucktest.model.SiteAnalyticsCriteria;
import com.adverity.grucktest.repository.SiteAnalyticsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer above SiteAnalyticsRepository, containing business logic above
 * DB layer.
 */
@Service
public class SiteAnalyticsService {

  SiteAnalyticsRepository siteAnalyticsRepository;

  @Autowired
  public SiteAnalyticsService(
      SiteAnalyticsRepository siteAnalyticsRepository) {
    this.siteAnalyticsRepository = siteAnalyticsRepository;
  }

  public List<SiteAnalytics> getSiteAnalytics(
      SiteAnalyticsCriteria siteAnalyticsCriteria) {
    return siteAnalyticsRepository.findSiteAnalytics(siteAnalyticsCriteria);
  }

  public List<SiteAnalytics> getSiteAnalyticsStats(
      SiteAnalyticsCriteria siteAnalyticsCriteria) {
    return siteAnalyticsRepository
        .findSumOfSiteAnalyticsStats(siteAnalyticsCriteria);
  }
}
