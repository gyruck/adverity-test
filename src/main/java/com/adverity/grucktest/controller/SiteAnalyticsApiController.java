package com.adverity.grucktest.controller;

import com.adverity.grucktest.model.SiteAnalytics;
import com.adverity.grucktest.model.SiteAnalyticsCriteria;
import com.adverity.grucktest.service.SiteAnalyticsService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API for site analytics.
 */
@RestController
@RequestMapping("/api/site-analytics")
public class SiteAnalyticsApiController {

  private final SiteAnalyticsService siteAnalyticsService;

  /**
   * Initialising site analytics controller.
   *
   * @param siteAnalyticsService Service for fetching data from repository
   */
  @Autowired
  public SiteAnalyticsApiController(
      SiteAnalyticsService siteAnalyticsService) {
    this.siteAnalyticsService = siteAnalyticsService;
  }

  /**
   * Generic find endpoint for fetching analytics record based on optional
   * filters.
   *
   * @param datasource Filtering for datasource field
   * @param campaign Filtering for datasource field
   * @param from Filtering for date window, starting with from
   * @param to Filtering for date window, ending with to
   * @return Returning a list of SiteAnalytics records
   */
  @GetMapping("/find")
  @ResponseBody
  public List<SiteAnalytics> find(
      @RequestParam(value = "datasource", required = false) String datasource,
      @RequestParam(value = "campaign", required = false) String campaign,
      @RequestParam(value = "from", required = false)
      @DateTimeFormat(pattern = SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT) LocalDate from,
      @RequestParam(value = "to", required = false)
      @DateTimeFormat(pattern = SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT) LocalDate to
  ) {
    SiteAnalyticsCriteria siteAnalyticsCriteria = SiteAnalyticsCriteria
        .builder()
        .campaign(campaign)
        .dataSource(datasource)
        .from(from)
        .to(to)
        .build();

    return siteAnalyticsService.getSiteAnalytics(siteAnalyticsCriteria);
  }

  /**
   * Grouped analytics records for given datasource. In case of datasource is
   * missing then no filtering will be applied.
   *
   * @param datasource Filtering for datasource field
   * @param campaign Filtering for datasource field
   * @param from Filtering for date window, starting with from
   * @param to Filtering for date window, ending with to
   * @return Returning a list of SiteAnalytics records
   */
  @GetMapping({"/datasource", "/datasource/{datasource}"})
  @ResponseBody
  public List<SiteAnalytics> getStatsByDataSource(
      @PathVariable(value = "datasource", required = false) String datasource,
      @RequestParam(value = "campaign", required = false) String campaign,
      @RequestParam(value = "from", required = false)
      @DateTimeFormat(pattern = SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT) LocalDate from,
      @RequestParam(value = "to", required = false)
      @DateTimeFormat(pattern = SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT) LocalDate to
  ) {
    SiteAnalyticsCriteria siteAnalyticsCriteria = SiteAnalyticsCriteria
        .builder()
        .campaign(campaign)
        .dataSource(datasource)
        .from(from)
        .to(to)
        .build();

    return siteAnalyticsService.getSiteAnalyticsStats(siteAnalyticsCriteria);
  }

  /**
   * Grouped analytics records for given datasource and campaign. In case of
   * campaign is missing then no filtering will be applied.
   *
   * @param datasource Filtering for datasource field
   * @param campaign Filtering for datasource field
   * @param from Filtering for date window, starting with from
   * @param to Filtering for date window, ending with to
   * @return Returning a list of SiteAnalytics records
   */
  @GetMapping("/datasource/{datasource}/campaign/{campaign}")
  @ResponseBody
  public List<SiteAnalytics> getStatsByDataSourceAndCampaign(
      @PathVariable(value = "datasource") String datasource,
      @PathVariable(value = "campaign", required = false) String campaign,
      @RequestParam(value = "from", required = false)
      @DateTimeFormat(pattern = SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT) LocalDate from,
      @RequestParam(value = "to", required = false)
      @DateTimeFormat(pattern = SiteAnalytics.SITE_ANALYTICS_DATE_TIME_FORMAT) LocalDate to
  ) {
    SiteAnalyticsCriteria siteAnalyticsCriteria = SiteAnalyticsCriteria
        .builder()
        .campaign(campaign)
        .dataSource(datasource)
        .from(from)
        .to(to)
        .build();

    return siteAnalyticsService.getSiteAnalyticsStats(siteAnalyticsCriteria);
  }

}
