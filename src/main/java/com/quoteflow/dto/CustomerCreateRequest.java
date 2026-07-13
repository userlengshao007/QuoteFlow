package com.quoteflow.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 人员信息创建请求。
 *
 * @author zhangyujie
 */
public class CustomerCreateRequest {

    /**
     * 客户名称。
     */
    @NotBlank
    private String customerName;

    /**
     * 统一社会信用代码。
     */
    @NotBlank
    private String creditCode;

    /**
     * 客户级别。
     */
    @NotBlank
    private String customerLevel;

    /**
     * 主要联系人 UID。
     */
    @NotNull
    private Long mainContactId;

    /**
     * 主要联系人姓名。
     */
    @NotBlank
    private String mainContactName;

    /**
     * 客户所属行业。
     */
    @NotBlank
    private String industry;

    /**
     * 调用方用于幂等控制的业务唯一 ID。
     */
    private String uuid;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
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
