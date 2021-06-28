package com.adverity.grucktest.repository;

import com.adverity.grucktest.model.SiteAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DB layer for handling SiteAnalytics records.
 */
@Repository
public interface SiteAnalyticsRepository extends
    JpaRepository<SiteAnalytics, Long>, SiteAnalyticsCustomRepository {

}
