package com.adverity.grucktest.repository;

import com.adverity.grucktest.model.SiteAnalytics;
import com.adverity.grucktest.model.SiteAnalyticsCriteria;
import java.util.List;

/**
 * Custom repository for SiteAnalytics entities, adding extra methods above
 * standard JPA queries.
 */
public interface SiteAnalyticsCustomRepository {

  List<SiteAnalytics> findSiteAnalytics(
      SiteAnalyticsCriteria siteAnalyticsCriteria);

  List<SiteAnalytics> findSumOfSiteAnalyticsStats(
      SiteAnalyticsCriteria siteAnalyticsCriteria);
}
