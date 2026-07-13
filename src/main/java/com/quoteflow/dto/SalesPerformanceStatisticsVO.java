package com.quoteflow.dto;

import java.math.BigDecimal;

/**
 * 销售业绩统计响应。
 *
 * @author zhangyujie
 */
public class SalesPerformanceStatisticsVO {

    /**
     * 统计表单数据 ID。
     */
    private Long formUserId;

    /**
     * 统计编号。
     */
    private String statisticsId;

    /**
     * 负责客户数。
     */
    private Integer responsibleCustomers;

    /**
     * 项目数量。
     */
    private Integer projectCount;

    /**
     * 报价总额。
     */
    private BigDecimal totalQuoteAmount;

    /**
     * 平均项目金额。
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
