package com.quoteflow.dto;

import java.math.BigDecimal;

/**
 * 客户报价统计明细。
 *
 * @author zhangyujie
 */
public class CustomerQuoteStatsItemVO {

    /**
     * 客户 ID。
     */
    private String customerId;

    /**
     * 客户名称。
     */
    private String customerName;

    /**
     * 项目数量。
     */
    private Integer projectCount;

    /**
     * 报价总额。
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
