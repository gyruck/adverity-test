package com.adverity.grucktest.helper;

/**
 * Headers for loading site analytics CSV.
 */
public enum SiteAnalyticsCsvHeaders {
  DATASOURCE("Datasource"),
  CAMPAIGN("Campaign"),
  DAILY("Daily"),
  CLICKS("Clicks"),
  IMPRESSIONS("Impressions");

  private String header;

  SiteAnalyticsCsvHeaders(String header) {
    this.header = header;
  }

  public String getHeader() {
    return header;
  }
}
