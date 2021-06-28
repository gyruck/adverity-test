package com.adverity.grucktest.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adverity.grucktest.model.SiteAnalytics;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Testing SiteAnalyticsController API contract.
 */
@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class SiteAnalyticsControllerTests {

  private static final String DATASOURCE = "Google Ads";
  private static final String CAMPAIGN = "Adventmarkt Touristik";

  @Autowired
  private WebApplicationContext context;

  @PersistenceContext
  private EntityManager entityManager;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(restDocumentation)).build();
  }

  @Test
  public void testFind() throws Exception {
    givenExampleSiteAnalyticsRecords();

    this.mockMvc.perform(get("/api/site-analytics/find")
        .queryParam("datasource", DATASOURCE)
        .queryParam("campaign", CAMPAIGN)
    ).andExpect(status().isOk())
        .andDo(document("sa-find", requestParameters(
            parameterWithName("datasource").optional()
                .description("Name of the datasource. e.g. 'Google Ads'"),
            parameterWithName("campaign").optional().description(
                "Name of the campaign, e.g. 'Adventmarkt Touristik'"),
            parameterWithName("from").optional()
                .description("Start date for filtering, e.g. '11/14/19'"),
            parameterWithName("to").optional()
                .description("Ending date for filtering, e.g. '11/17/20'")
            ), responseFields(
            fieldWithPath("[].id")
                .description("ID of the site analytics record"),
            fieldWithPath("[].datasource")
                .description("Datasource of the tracking analytics"),
            fieldWithPath("[].campaign")
                .description("Campaign of the tracking analytics"),
            fieldWithPath("[].daily").description("Day of the aggregation"),
            fieldWithPath("[].clicks")
                .description("Number of clicks for the given day"),
            fieldWithPath("[].impressions")
                .description("How often someone saw the click on the site"),
            fieldWithPath("[].clickThroughRate")
                .description("Ratio coming from Clicks / Impressions")
            )
        ));
  }

  @Test
  public void getStatsByDataSource() throws Exception {
    givenExampleSiteAnalyticsRecords();

    this.mockMvc
        .perform(get("/api/site-analytics/datasource/{datasource}", DATASOURCE))
        .andExpect(status().isOk())
        .andDo(document("sa-datasource", requestParameters(
            parameterWithName("from").optional()
                .description("Start date for filtering, e.g. '11/14/19'"),
            parameterWithName("to").optional()
                .description("Ending date for filtering, e.g. '11/17/20'")
            ), responseFields(
            fieldWithPath("[].datasource")
                .description("Datasource of the tracking analytics"),
            fieldWithPath("[].campaign")
                .description("Campaign of the tracking analytics"),
            fieldWithPath("[].clicks")
                .description("Number of clicks for the given day"),
            fieldWithPath("[].impressions")
                .description("How often someone saw the click on the site"),
            fieldWithPath("[].clickThroughRate")
                .description("Ratio coming from Clicks / Impressions")
            )
        ));
  }

  @Test
  public void getStatsByCampaign() throws Exception {
    givenExampleSiteAnalyticsRecords();

    this.mockMvc.perform(
        get("/api/site-analytics/datasource/{datasource}/campaign/{campaign}",
            DATASOURCE, CAMPAIGN))
        .andExpect(status().isOk())
        .andDo(document("sa-campaign", pathParameters(
            parameterWithName("datasource").optional()
                .description("Name of the datasource. e.g. 'Google Ads'"),
            parameterWithName("campaign").optional().description(
                "Name of the campaign, e.g. 'Adventmarkt Touristik'")
            ), requestParameters(
            parameterWithName("from").optional()
                .description("Start date for filtering, e.g. '11/14/19'"),
            parameterWithName("to").optional()
                .description("Ending date for filtering, e.g. '11/17/20'")
            ), responseFields(
            fieldWithPath("[].datasource")
                .description("Datasource of the tracking analytics"),
            fieldWithPath("[].campaign")
                .description("Campaign of the tracking analytics"),
            fieldWithPath("[].clicks")
                .description("Number of clicks for the given day"),
            fieldWithPath("[].impressions")
                .description("How often someone saw the click on the site"),
            fieldWithPath("[].clickThroughRate")
                .description("Ratio coming from Clicks / Impressions")
            )
        ));
  }


  private void givenExampleSiteAnalyticsRecords() {
    List<SiteAnalytics> siteAnalyticsList = new ArrayList<>();

    siteAnalyticsList.add(
        new SiteAnalytics(DATASOURCE, CAMPAIGN, "11/12/19", 7,
            22425));
    siteAnalyticsList.add(
        new SiteAnalytics("Twitter Ads", "DE | SP Gadgets", "01/04/19", 81,
            1317));

    for (SiteAnalytics siteAnalytics : siteAnalyticsList) {
      entityManager.persist(siteAnalytics);
    }
    entityManager.flush();
    entityManager.clear();
  }
}
