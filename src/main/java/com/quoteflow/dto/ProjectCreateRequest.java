package com.quoteflow.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Project approval creation request.
 *
 * @author QuoteFlow
 */
public class ProjectCreateRequest {

    /**
     * Related customer id.
     */
    @NotBlank
    private String customerId;

    /**
     * Project date string accepted by Chaoxing date field.
     */
    @NotBlank
    private String projectDate;

    /**
     * Expected finish date string accepted by Chaoxing date field.
     */
    @NotBlank
    private String expectedFinishDate;

    /**
     * Project members.
     */
    @Valid
    @NotEmpty
    private List<ContactDTO> projectMembers;

    /**
     * Quote detail rows.
     */
    @Valid
    @NotEmpty
    private List<QuoteDetailRequest> quoteDetails;

    /**
     * Caller business unique id used for idempotency.
     */
    private String uuid;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProjectDate() {
        return projectDate;
    }

    public void setProjectDate(String projectDate) {
        this.projectDate = projectDate;
    }

    public String getExpectedFinishDate() {
        return expectedFinishDate;
    }

    public void setExpectedFinishDate(String expectedFinishDate) {
        this.expectedFinishDate = expectedFinishDate;
    }

    public List<ContactDTO> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<ContactDTO> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public List<QuoteDetailRequest> getQuoteDetails() {
        return quoteDetails;
    }

    public void setQuoteDetails(List<QuoteDetailRequest> quoteDetails) {
        this.quoteDetails = quoteDetails;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Calculates quote total amount.
     *
     * @return quote total amount
     */
    public BigDecimal calculateTotalQuoteAmount() {
        return quoteDetails.stream()
                .map(QuoteDetailRequest::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
