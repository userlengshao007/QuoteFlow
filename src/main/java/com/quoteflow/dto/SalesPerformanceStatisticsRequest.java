package com.quoteflow.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import org.springframework.util.StringUtils;

/**
 * 销售业绩统计请求。
 *
 * @author zhangyujie
 */
public class SalesPerformanceStatisticsRequest {

    /**
     * 日期格式。
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 客户级别筛选条件。
     */
    private String customerLevel;

    /**
     * 超星日期字段可接受的项目日期开始值。
     */
    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}", message = "格式必须为 yyyy-MM-dd")
    private String projectDateStart;

    /**
     * 超星日期字段可接受的项目日期结束值。
     */
    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}", message = "格式必须为 yyyy-MM-dd")
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

    /**
     * 判断项目日期范围是否完整。
     *
     * @return 日期范围是否完整
     */
    @AssertTrue(message = "项目立项日期开始和结束必须同时填写")
    public Boolean getProjectDateRangeComplete() {
        return StringUtils.hasText(projectDateStart) == StringUtils.hasText(projectDateEnd);
    }

    /**
     * 判断项目日期范围是否有效。
     *
     * @return 日期范围是否有效
     */
    @AssertTrue(message = "项目立项日期开始不能晚于结束")
    public Boolean getProjectDateRangeValid() {
        if (!StringUtils.hasText(projectDateStart) || !StringUtils.hasText(projectDateEnd)) {
            return Boolean.TRUE;
        }
        try {
            LocalDate startDate = LocalDate.parse(projectDateStart, DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(projectDateEnd, DATE_FORMATTER);
            return !startDate.isAfter(endDate);
        } catch (DateTimeParseException exception) {
            return Boolean.TRUE;
        }
    }
}
