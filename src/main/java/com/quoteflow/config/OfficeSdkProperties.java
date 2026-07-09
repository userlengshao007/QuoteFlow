package com.quoteflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Chaoxing office SDK and form mapping configuration.
 *
 * @author QuoteFlow
 */
@ConfigurationProperties(prefix = "chaoxing.office")
public class OfficeSdkProperties {

    /**
     * Chaoxing office API backend domain.
     */
    private String serverDomain;

    /**
     * Chaoxing office front domain.
     */
    private String serverFrontDomain;

    /**
     * Forms API sign.
     */
    private String formsSign;

    /**
     * Forms API key.
     */
    private String formsKey;

    /**
     * Approval API sign.
     */
    private String approveSign;

    /**
     * Approval API key.
     */
    private String approveKey;

    /**
     * Unit id required by Chaoxing SDK.
     */
    private Integer fid;

    /**
     * Default submitter uid used before user login integration is available.
     */
    private Long submitUid;

    /**
     * Form id mapping.
     */
    private Form form;

    /**
     * Field alias mapping.
     */
    private Field field;

    public String getServerDomain() {
        return serverDomain;
    }

    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    public String getServerFrontDomain() {
        return serverFrontDomain;
    }

    public void setServerFrontDomain(String serverFrontDomain) {
        this.serverFrontDomain = serverFrontDomain;
    }

    public String getFormsSign() {
        return formsSign;
    }

    public void setFormsSign(String formsSign) {
        this.formsSign = formsSign;
    }

    public String getFormsKey() {
        return formsKey;
    }

    public void setFormsKey(String formsKey) {
        this.formsKey = formsKey;
    }

    public String getApproveSign() {
        return approveSign;
    }

    public void setApproveSign(String approveSign) {
        this.approveSign = approveSign;
    }

    public String getApproveKey() {
        return approveKey;
    }

    public void setApproveKey(String approveKey) {
        this.approveKey = approveKey;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Long getSubmitUid() {
        return submitUid;
    }

    public void setSubmitUid(Long submitUid) {
        this.submitUid = submitUid;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    /**
     * Chaoxing form id mapping.
     *
     * @author QuoteFlow
     */
    public static class Form {

        /**
         * Customer information form id.
         */
        private Integer customerFormId;

        /**
         * Project approval form id.
         */
        private Integer projectApproveFormId;

        /**
         * Sales statistics form id.
         */
        private Integer salesStatisticsFormId;

        public Integer getCustomerFormId() {
            return customerFormId;
        }

        public void setCustomerFormId(Integer customerFormId) {
            this.customerFormId = customerFormId;
        }

        public Integer getProjectApproveFormId() {
            return projectApproveFormId;
        }

        public void setProjectApproveFormId(Integer projectApproveFormId) {
            this.projectApproveFormId = projectApproveFormId;
        }

        public Integer getSalesStatisticsFormId() {
            return salesStatisticsFormId;
        }

        public void setSalesStatisticsFormId(Integer salesStatisticsFormId) {
            this.salesStatisticsFormId = salesStatisticsFormId;
        }
    }

    /**
     * Chaoxing field alias mapping.
     *
     * @author QuoteFlow
     */
    public static class Field {

        /**
         * Customer id alias.
         */
        private String customerId;

        /**
         * Customer name alias.
         */
        private String customerName;

        /**
         * Credit code alias.
         */
        private String creditCode;

        /**
         * Customer level alias.
         */
        private String customerLevel;

        /**
         * Main contact alias.
         */
        private String mainContact;

        /**
         * Industry alias.
         */
        private String industry;

        /**
         * Attached files alias.
         */
        private String attachedFiles;

        /**
         * Project id alias.
         */
        private String projectId;

        /**
         * Project code alias.
         */
        private String projectCode;

        /**
         * Project date alias.
         */
        private String projectDate;

        /**
         * Expected finish date alias.
         */
        private String expectedFinishDate;

        /**
         * Project members alias.
         */
        private String projectMembers;

        /**
         * Quote details alias.
         */
        private String quoteDetails;

        /**
         * Total quote amount alias.
         */
        private String totalQuoteAmount;

        /**
         * Quote attachment alias.
         */
        private String quoteAttachment;

        /**
         * Project status alias.
         */
        private String projectStatus;

        /**
         * Product name alias.
         */
        private String productName;

        /**
         * Specification alias.
         */
        private String specification;

        /**
         * Quantity alias.
         */
        private String quantity;

        /**
         * Unit price alias.
         */
        private String unitPrice;

        /**
         * Subtotal alias.
         */
        private String subtotal;

        /**
         * Performance number alias.
         */
        private String performanceNo;

        /**
         * Sales contact alias.
         */
        private String salesContact;

        /**
         * Quote total alias.
         */
        private String quoteTotal;

        /**
         * Statistics id alias.
         */
        private String statisticsId;

        /**
         * Statistics date alias.
         */
        private String statisticsDate;

        /**
         * Sales person alias.
         */
        private String salesPerson;

        /**
         * Responsible customers alias.
         */
        private String responsibleCustomers;

        /**
         * Project count alias.
         */
        private String projectCount;

        /**
         * Average project amount alias.
         */
        private String averageProjectAmount;

        /**
         * Search condition alias.
         */
        private String searchCondition;

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

        public String getMainContact() {
            return mainContact;
        }

        public void setMainContact(String mainContact) {
            this.mainContact = mainContact;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getAttachedFiles() {
            return attachedFiles;
        }

        public void setAttachedFiles(String attachedFiles) {
            this.attachedFiles = attachedFiles;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
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

        public String getProjectMembers() {
            return projectMembers;
        }

        public void setProjectMembers(String projectMembers) {
            this.projectMembers = projectMembers;
        }

        public String getQuoteDetails() {
            return quoteDetails;
        }

        public void setQuoteDetails(String quoteDetails) {
            this.quoteDetails = quoteDetails;
        }

        public String getTotalQuoteAmount() {
            return totalQuoteAmount;
        }

        public void setTotalQuoteAmount(String totalQuoteAmount) {
            this.totalQuoteAmount = totalQuoteAmount;
        }

        public String getQuoteAttachment() {
            return quoteAttachment;
        }

        public void setQuoteAttachment(String quoteAttachment) {
            this.quoteAttachment = quoteAttachment;
        }

        public String getProjectStatus() {
            return projectStatus;
        }

        public void setProjectStatus(String projectStatus) {
            this.projectStatus = projectStatus;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getPerformanceNo() {
            return performanceNo;
        }

        public void setPerformanceNo(String performanceNo) {
            this.performanceNo = performanceNo;
        }

        public String getSalesContact() {
            return salesContact;
        }

        public void setSalesContact(String salesContact) {
            this.salesContact = salesContact;
        }

        public String getQuoteTotal() {
            return quoteTotal;
        }

        public void setQuoteTotal(String quoteTotal) {
            this.quoteTotal = quoteTotal;
        }

        public String getStatisticsId() {
            return statisticsId;
        }

        public void setStatisticsId(String statisticsId) {
            this.statisticsId = statisticsId;
        }

        public String getStatisticsDate() {
            return statisticsDate;
        }

        public void setStatisticsDate(String statisticsDate) {
            this.statisticsDate = statisticsDate;
        }

        public String getSalesPerson() {
            return salesPerson;
        }

        public void setSalesPerson(String salesPerson) {
            this.salesPerson = salesPerson;
        }

        public String getResponsibleCustomers() {
            return responsibleCustomers;
        }

        public void setResponsibleCustomers(String responsibleCustomers) {
            this.responsibleCustomers = responsibleCustomers;
        }

        public String getProjectCount() {
            return projectCount;
        }

        public void setProjectCount(String projectCount) {
            this.projectCount = projectCount;
        }

        public String getAverageProjectAmount() {
            return averageProjectAmount;
        }

        public void setAverageProjectAmount(String averageProjectAmount) {
            this.averageProjectAmount = averageProjectAmount;
        }

        public String getSearchCondition() {
            return searchCondition;
        }

        public void setSearchCondition(String searchCondition) {
            this.searchCondition = searchCondition;
        }
    }
}
