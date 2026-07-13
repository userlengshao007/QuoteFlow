package com.quoteflow.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 项目审批创建请求。
 *
 * @author zhangyujie
 */
public class ProjectCreateRequest {

    /**
     * 关联客户 ID。
     */
    @NotBlank
    private String customerId;

    /**
     * 超星日期字段可接受的立项日期字符串。
     */
    @NotBlank
    private String projectDate;

    /**
     * 超星日期字段可接受的预计完成日期字符串。
     */
    @NotBlank
    private String expectedFinishDate;

    /**
     * 项目成员。
     */
    @Valid
    @NotEmpty
    private List<ContactDTO> projectMembers;

    /**
     * 报价明细行。
     */
    @Valid
    @NotEmpty
    private List<QuoteDetailRequest> quoteDetails;

    /**
     * 调用方用于幂等控制的业务唯一 ID。
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
     * 计算报价总额。
     *
     * @return 报价总额
     */
    public BigDecimal calculateTotalQuoteAmount() {
        return quoteDetails.stream()
                .map(QuoteDetailRequest::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
