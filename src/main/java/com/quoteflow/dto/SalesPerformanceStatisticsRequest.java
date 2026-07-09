package com.quoteflow.dto;

/**
 * Sales performance statistics request.
 *
 * @author QuoteFlow
 */
public class SalesPerformanceStatisticsRequest {

    /**
     * Customer level filter.
     */
    private String customerLevel;

    /**
     * Project date start, accepted by Chaoxing date field.
     */
    private String projectDateStart;

    /**
     * Project date end, accepted by Chaoxing date field.
     */
    private String projectDateEnd;

    /**
     * Sales name fuzzy filter.
     */
    private String salesName;

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getProjectDateStart() {
        return projectDateStart;
    }

    public void setProjectDateStart(String projectDateStart) {
        this.projectDateStart = projectDateStart;
    }

    public String getProjectDateEnd() {
        return projectDateEnd;
    }

    public void setProjectDateEnd(String projectDateEnd) {
        this.projectDateEnd = projectDateEnd;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }
}
