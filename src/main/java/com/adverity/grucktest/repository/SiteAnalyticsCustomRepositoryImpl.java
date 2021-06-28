package com.adverity.grucktest.repository;

import com.adverity.grucktest.model.SiteAnalytics;
import com.adverity.grucktest.model.SiteAnalyticsCriteria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Concrete implementation of advanced queries for SiteAnalytics entities.
 */
public class SiteAnalyticsCustomRepositoryImpl implements
    SiteAnalyticsCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Finding site analytics records based on filters.
   *
   * @param siteAnalyticsCriteria Available filters for the search
   * @return List of SiteAnalytics entities
   */
  @Override
  public List<SiteAnalytics> findSiteAnalytics(
      SiteAnalyticsCriteria siteAnalyticsCriteria) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<SiteAnalytics> query = cb.createQuery(SiteAnalytics.class);
    Root<SiteAnalytics> siteAnalytics = query.from(SiteAnalytics.class);

    Predicate[] predicates = siteAnalyticsCriteria
        .convertToPredicates(siteAnalytics, cb);

    query.select(siteAnalytics)
        .where(cb.and(predicates));

    return entityManager.createQuery(query)
        .getResultList();
  }

  /**
   * Finding aggregated results for site analytics. Grouping by datasources and
   * / or campaigns.
   *
   * @param siteAnalyticsCriteria Available filters for the search
   * @return List of SiteAnalytics entities
   */
  @Override
  public List<SiteAnalytics> findSumOfSiteAnalyticsStats(
      SiteAnalyticsCriteria siteAnalyticsCriteria) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<SiteAnalytics> query = cb
        .createQuery(SiteAnalytics.class);
    Root<SiteAnalytics> siteAnalytics = query.from(SiteAnalytics.class);

    Predicate[] wherePredicates = siteAnalyticsCriteria
        .convertToPredicates(siteAnalytics, cb);

    Expression<String> datasourcePath = siteAnalytics.get("datasource");
    Expression<String> campaignPath = siteAnalytics.get("campaign");

    query.multiselect(
        datasourcePath,
        campaignPath,
        cb.sum(siteAnalytics.get("clicks")),
        cb.sum(siteAnalytics.get("impressions")))
        .where(cb.and(wherePredicates))
        .groupBy(datasourcePath, campaignPath);

    return entityManager.createQuery(query)
        .getResultList();
  }
}
