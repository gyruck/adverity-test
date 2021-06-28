package com.adverity.grucktest.model;

import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Available search filters for finding SiteAnalytics entities.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteAnalyticsCriteria {

  private String campaign;
  private String dataSource;
  private LocalDate from;
  private LocalDate to;
  private int limit;

  /**
   * Converting available filters to predicates which could be used in e.g.
   * where conditions for queries.
   *
   * @param siteAnalytics SiteAnalytics root
   * @param cb CriteraBuilder
   * @return List of predicates created from input criteria
   */
  public Predicate[] convertToPredicates(
      Root<SiteAnalytics> siteAnalytics, CriteriaBuilder cb) {
    List<Predicate> predicates = new ArrayList<>();

    if (hasText(dataSource)) {
      predicates
          .add(cb.equal(siteAnalytics.get("datasource"), dataSource));
    }
    if (hasText(campaign)) {
      predicates
          .add(cb.equal(siteAnalytics.get("campaign"), campaign));
    }
    if (from != null) {
      predicates.add(cb.greaterThanOrEqualTo(siteAnalytics.get("daily"),
          from));
    }

    if (to != null) {
      predicates.add(cb.lessThanOrEqualTo(siteAnalytics.get("daily"),
          to));
    }

    return predicates.toArray(new Predicate[0]);
  }
}
