package com.quoteflow.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Customer creation request.
 *
 * @author QuoteFlow
 */
public class CustomerCreateRequest {

    /**
     * Customer name.
     */
    @NotBlank
    private String customerName;

    /**
     * Customer level.
     */
    @NotBlank
    private String customerLevel;

    /**
     * Main contact uid.
     */
    @NotNull
    private Long mainContactId;

    /**
     * Main contact name.
     */
    @NotBlank
    private String mainContactName;

    /**
     * Customer industry.
     */
    @NotBlank
    private String industry;

    /**
     * Caller business unique id used for idempotency.
     */
    private String uuid;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public Long getMainContactId() {
        return mainContactId;
    }

    public void setMainContactId(Long mainContactId) {
        this.mainContactId = mainContactId;
    }

    public String getMainContactName() {
        return mainContactName;
    }

    public void setMainContactName(String mainContactName) {
        this.mainContactName = mainContactName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
