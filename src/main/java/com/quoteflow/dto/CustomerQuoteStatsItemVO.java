package com.quoteflow.dto;

import java.math.BigDecimal;

/**
 * Customer quote statistics item.
 *
 * @author QuoteFlow
 */
public class CustomerQuoteStatsItemVO {

    /**
     * Customer id.
     */
    private String customerId;

    /**
     * Customer name.
     */
    private String customerName;

    /**
     * Project count.
     */
    private Integer projectCount;

    /**
     * Total quote amount.
     */
    private BigDecimal totalQuoteAmount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
}
