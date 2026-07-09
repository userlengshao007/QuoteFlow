package com.quoteflow.dto;

import java.math.BigDecimal;

/**
 * Sales performance statistics response.
 *
 * @author QuoteFlow
 */
public class SalesPerformanceStatisticsVO {

    /**
     * Statistics form data id.
     */
    private Long formUserId;

    /**
     * Statistics number.
     */
    private String statisticsId;

    /**
     * Responsible customer count.
     */
    private Integer responsibleCustomers;

    /**
     * Project count.
     */
    private Integer projectCount;

    /**
     * Total quote amount.
     */
    private BigDecimal totalQuoteAmount;

    /**
     * Average project amount.
     */
    private BigDecimal averageProjectAmount;

    public Long getFormUserId() {
        return formUserId;
    }

    public void setFormUserId(Long formUserId) {
        this.formUserId = formUserId;
    }

    public String getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(String statisticsId) {
        this.statisticsId = statisticsId;
    }

    public Integer getResponsibleCustomers() {
        return responsibleCustomers;
    }

    public void setResponsibleCustomers(Integer responsibleCustomers) {
        this.responsibleCustomers = responsibleCustomers;
    }

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public BigDecimal getTotalQuoteAmount() {
        return totalQuoteAmount;
    }

    public void setTotalQuoteAmount(BigDecimal totalQuoteAmount) {
        this.totalQuoteAmount = totalQuoteAmount;
    }

    public BigDecimal getAverageProjectAmount() {
        return averageProjectAmount;
    }

    public void setAverageProjectAmount(BigDecimal averageProjectAmount) {
        this.averageProjectAmount = averageProjectAmount;
    }
}
