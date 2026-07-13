package com.quoteflow.dto;

/**
 * 项目审批推送处理结果。
 *
 * @author zhangyujie
 */
public class ProjectApprovalPushResultVO {

    /**
     * 是否已生成销售业绩。
     */
    private Boolean processed;

    /**
     * 处理说明。
     */
    private String message;

    /**
     * 项目审批数据 ID。
     */
    private Long projectFormUserId;

    /**
     * 销售业绩表数据 ID。
     */
    private Long performanceFormUserId;

    /**
     * 重复提交时超星返回的销售业绩表数据 ID。
     */
    private Long repeatPerformanceFormUserId;

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getProjectFormUserId() {
        return projectFormUserId;
    }

    public void setProjectFormUserId(Long projectFormUserId) {
        this.projectFormUserId = projectFormUserId;
    }

    public Long getPerformanceFormUserId() {
        return performanceFormUserId;
    }

    public void setPerformanceFormUserId(Long performanceFormUserId) {
        this.performanceFormUserId = performanceFormUserId;
    }

    public Long getRepeatPerformanceFormUserId() {
        return repeatPerformanceFormUserId;
    }

    public void setRepeatPerformanceFormUserId(Long repeatPerformanceFormUserId) {
        this.repeatPerformanceFormUserId = repeatPerformanceFormUserId;
    }
}
