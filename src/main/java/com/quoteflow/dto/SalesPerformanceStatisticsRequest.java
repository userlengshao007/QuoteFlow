package com.quoteflow.dto;

/**
 * 销售业绩统计请求。
 *
 * @author zhangyujie
 */
public class SalesPerformanceStatisticsRequest {

    /**
     * 客户级别筛选条件。
     */
    private String customerLevel;

    /**
     * 超星日期字段可接受的项目日期开始值。
     */
    private String projectDateStart;

    /**
     * 超星日期字段可接受的项目日期结束值。
     */
    private String projectDateEnd;

    /**
     * 销售姓名模糊筛选条件。
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
