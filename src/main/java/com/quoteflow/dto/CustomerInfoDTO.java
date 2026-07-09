package com.quoteflow.dto;

/**
 * Customer information transfer object.
 *
 * @author QuoteFlow
 */
public class CustomerInfoDTO {

    /**
     * Customer id.
     */
    private String customerId;

    /**
     * Customer name.
     */
    private String customerName;

    /**
     * Customer level.
     */
    private String customerLevel;

    /**
     * Main contact.
     */
    private ContactDTO mainContact;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

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

    public ContactDTO getMainContact() {
        return mainContact;
    }

    public void setMainContact(ContactDTO mainContact) {
        this.mainContact = mainContact;
    }
}
