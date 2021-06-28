package com.adverity.grucktest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * SiteAnalytics entity, parsed from CSV.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class SiteAnalytics {

  public static final String SITE_ANALYTICS_DATE_TIME_FORMAT = "MM/dd/yy";
  private static final DateTimeFormatter DAILY_DATE_FORMATTER = DateTimeFormatter
      .ofPattern(SITE_ANALYTICS_DATE_TIME_FORMAT);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonInclude(Include.NON_DEFAULT)
  private long id;
  private String datasource;
  private String campaign;
  @JsonInclude(Include.NON_DEFAULT)
  private LocalDate daily;
  private long clicks;
  private long impressions;

  /**
   * Create SiteAnalytics entity.
   *
   * @param datasource Datasource of tracking
   * @param campaign Campaign of tracking
   * @param daily Date of results ("MM/dd/yy")
   * @param clicks Number of clicks
   * @param impressions Number of impressions
   */
  public SiteAnalytics(String datasource,
      String campaign, String daily, long clicks, long impressions) {
    this.datasource = datasource;
    this.campaign = campaign;
    this.daily = LocalDate.parse(daily, DAILY_DATE_FORMATTER);
    this.clicks = clicks;
    this.impressions = impressions;
  }

  /**
   * Create SiteAnalytics entity.
   *
   * @param datasource Datasource of tracking
   * @param campaign Campaign of tracking
   * @param clicks Number of clicks
   * @param impressions Number of impressions
   */
  public SiteAnalytics(String datasource,
      String campaign, long clicks, long impressions) {
    this.datasource = datasource;
    this.campaign = campaign;
    this.clicks = clicks;
    this.impressions = impressions;
  }

  /**
   * Create SiteAnalytics entity.
   *
   * @param datasource Datasource of tracking
   * @param clicks Number of clicks
   * @param impressions Number of impressions
   */
  public SiteAnalytics(String datasource, long clicks, long impressions) {
    this.datasource = datasource;
    this.clicks = clicks;
    this.impressions = impressions;
  }

  /**
   * Calculating click through rate: clicks / impressions.
   *
   * @return Click through rate in percent
   */
  public float getClickThroughRate() {
    if (impressions == 0) {
      return 0.0f;
    }

    return (float) clicks / impressions;
  }
}
