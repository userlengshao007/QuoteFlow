package com.quoteflow.dto;

/**
 * 客户信息传输对象。
 *
 * @author QuoteFlow
 */
public class CustomerInfoDTO {

    /**
     * 客户 ID。
     */
    private String customerId;

    /**
     * 客户名称。
     */
    private String customerName;

    /**
     * 客户级别。
     */
    private String customerLevel;

    /**
     * 主要联系人。
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
