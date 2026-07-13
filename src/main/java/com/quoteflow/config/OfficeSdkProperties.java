package com.quoteflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 超星办公 SDK 与表单映射配置。
 *
 * @author QuoteFlow
 */
@ConfigurationProperties(prefix = "chaoxing.office")
public class OfficeSdkProperties {

    /**
     * 超星办公 API 后端域名。
     */
    private String serverDomain;

    /**
     * 超星办公前端域名。
     */
    private String serverFrontDomain;

    /**
     * 表单 API sign。
     */
    private String formsSign;

    /**
     * 表单 API key。
     */
    private String formsKey;

    /**
     * 审批 API sign。
     */
    private String approveSign;

    /**
     * 审批 API key。
     */
    private String approveKey;

    /**
     * 顶部按钮追加加密串 key。
     */
    private String topButtonKey;

    /**
     * 超星 SDK 必填的单位 ID。
     */
    private Integer fid;

    /**
     * 接入登录前使用的默认提交人 UID。
     */
    private Long submitUid;

    /**
     * 表单 ID 映射。
     */
    private Form form;

    /**
     * 字段别名映射。
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

    public String getTopButtonKey() {
        return topButtonKey;
    }

    public void setTopButtonKey(String topButtonKey) {
        this.topButtonKey = topButtonKey;
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
     * 超星表单 ID 映射。
     *
     * @author QuoteFlow
     */
    public static class Form {

        /**
         * 人员信息表 ID。
         */
        private Integer customerFormId;

        /**
         * 项目立项与报价审批表 ID。
         */
        private Integer projectApproveFormId;

        /**
         * 销售业绩统计表 ID。
         */
        private Integer salesStatisticsFormId;

        /**
         * 销售业绩明细表 ID。
         */
        private Integer salesPerformanceFormId;

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

        public Integer getSalesPerformanceFormId() {
            return salesPerformanceFormId;
        }

        public void setSalesPerformanceFormId(Integer salesPerformanceFormId) {
            this.salesPerformanceFormId = salesPerformanceFormId;
        }
    }

    /**
     * 超星字段别名映射。
     *
     * @author QuoteFlow
     */
    public static class Field {

        /**
         * 客户 ID 字段别名。
         */
        private String customerId;

        /**
         * 客户名称字段别名。
         */
        private String customerName;

        /**
         * 统一信用代码字段别名。
         */
        private String creditCode;

        /**
         * 客户级别字段别名。
         */
        private String customerLevel;

        /**
         * 主要联系人字段别名。
         */
        private String mainContact;

        /**
         * 所属行业字段别名。
         */
        private String industry;

        /**
         * 资质文件字段别名。
         */
        private String attachedFiles;

        /**
         * 项目 ID 字段别名。
         */
        private String projectId;

        /**
         * 项目编号字段别名。
         */
        private String projectCode;

        /**
         * 立项日期字段别名。
         */
        private String projectDate;

        /**
         * 预计完成日期字段别名。
         */
        private String expectedFinishDate;

        /**
         * 项目成员字段别名。
         */
        private String projectMembers;

        /**
         * 报价明细字段别名。
         */
        private String quoteDetails;

        /**
         * 报价总额字段别名。
         */
        private String totalQuoteAmount;

        /**
         * 报价单附件字段别名。
         */
        private String quoteAttachment;

        /**
         * 项目状态字段别名。
         */
        private String projectStatus;

        /**
         * 产品名称字段别名。
         */
        private String productName;

        /**
         * 规格型号字段别名。
         */
        private String specification;

        /**
         * 数量字段别名。
         */
        private String quantity;

        /**
         * 单价字段别名。
         */
        private String unitPrice;

        /**
         * 小计字段别名。
         */
        private String subtotal;

        /**
         * 业绩编号字段别名。
         */
        private String performanceNo;

        /**
         * 销售联系人字段别名。
         */
        private String salesContact;

        /**
         * 项目报价总额字段别名。
         */
        private String quoteTotal;

        /**
         * 统计编号字段别名。
         */
        private String statisticsId;

        /**
         * 统计日期字段别名。
         */
        private String statisticsDate;

        /**
         * 销售人员字段别名。
         */
        private String salesPerson;

        /**
         * 负责客户数字段别名。
         */
        private String responsibleCustomers;

        /**
         * 立项项目数字段别名。
         */
        private String projectCount;

        /**
         * 平均项目金额字段别名。
         */
        private String averageProjectAmount;

        /**
         * 筛选条件字段别名。
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
