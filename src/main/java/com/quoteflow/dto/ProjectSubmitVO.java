package com.quoteflow.dto;

/**
 * 项目审批提交响应视图对象。
 *
 * @author QuoteFlow
 */
public class ProjectSubmitVO {

    /**
     * 超星审批数据 ID。
     */
    private Long formUserId;

    /**
     * 审批详情页地址。
     */
    private String detailUrl;

    public Long getFormUserId() {
        return formUserId;
    }

    public void setFormUserId(Long formUserId) {
        this.formUserId = formUserId;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
