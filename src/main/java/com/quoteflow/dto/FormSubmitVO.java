package com.quoteflow.dto;

/**
 * 表单提交响应视图对象。
 *
 * @author QuoteFlow
 */
public class FormSubmitVO {

    /**
     * 超星表单数据 ID。
     */
    private Long formUserId;

    /**
     * 超星返回的重复表单数据 ID。
     */
    private Long repeatFormUserId;

    public Long getFormUserId() {
        return formUserId;
    }

    public void setFormUserId(Long formUserId) {
        this.formUserId = formUserId;
    }

    public Long getRepeatFormUserId() {
        return repeatFormUserId;
    }

    public void setRepeatFormUserId(Long repeatFormUserId) {
        this.repeatFormUserId = repeatFormUserId;
    }
}
