package com.quoteflow.dto;

/**
 * Form submission response view object.
 *
 * @author QuoteFlow
 */
public class FormSubmitVO {

    /**
     * Chaoxing form data id.
     */
    private Long formUserId;

    /**
     * Repeated form data id returned by Chaoxing.
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
