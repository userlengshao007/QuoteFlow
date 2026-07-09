package com.quoteflow.dto;

import javax.validation.constraints.NotBlank;

/**
 * Customer quote statistics request.
 *
 * @author QuoteFlow
 */
public class CustomerQuoteStatsRequest {

    /**
     * Selected project form user ids, separated by comma.
     */
    @NotBlank
    private String formUserIds;

    public String getFormUserIds() {
        return formUserIds;
    }

    public void setFormUserIds(String formUserIds) {
        this.formUserIds = formUserIds;
    }
}
