package com.quoteflow.dto;

/**
 * Project approval submission response view object.
 *
 * @author QuoteFlow
 */
public class ProjectSubmitVO {

    /**
     * Chaoxing approval data id.
     */
    private Long formUserId;

    /**
     * Approval detail page url.
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
