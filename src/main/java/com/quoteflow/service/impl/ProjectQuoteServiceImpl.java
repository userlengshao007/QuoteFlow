package com.quoteflow.service.impl;

import com.chaoxing.office.app.entity.forms.dto.data.FormsData;
import com.chaoxing.office.app.entity.forms.dto.data.field.ContactField;
import com.chaoxing.office.app.entity.forms.dto.data.field.DateinputField;
import com.chaoxing.office.app.entity.forms.dto.data.field.DetailField;
import com.chaoxing.office.app.entity.forms.dto.data.field.EditinputField;
import com.chaoxing.office.app.entity.forms.dto.data.field.NumberinputField;
import com.chaoxing.office.app.entity.forms.dto.data.field.SelectBoxField;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.quoteflow.client.ChaoxingOfficeClient;
import com.quoteflow.config.OfficeSdkProperties;
import com.quoteflow.dto.ContactDTO;
import com.quoteflow.dto.ProjectCreateRequest;
import com.quoteflow.dto.ProjectSubmitVO;
import com.quoteflow.dto.QuoteDetailRequest;
import com.quoteflow.service.ProjectQuoteService;
import com.quoteflow.util.ChaoxingResponseUtils;
import com.quoteflow.util.ConfigurationAssert;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * 项目立项与报价服务实现。
 *
 * @author QuoteFlow
 */
@Service
public class ProjectQuoteServiceImpl implements ProjectQuoteService {

    /**
     * 初始项目状态。
     */
    private static final String PROJECT_STATUS_INITIATED = "立项";

    /**
     * 项目编号前缀。
     */
    private static final String PROJECT_CODE_PREFIX = "PROJ-";

    /**
     * 项目编号年份格式。
     */
    private static final DateTimeFormatter PROJECT_CODE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy");

    /**
     * 超星办公客户端。
     */
    private final ChaoxingOfficeClient chaoxingOfficeClient;

    /**
     * 超星 SDK 配置。
     */
    private final OfficeSdkProperties officeSdkProperties;

    public ProjectQuoteServiceImpl(ChaoxingOfficeClient chaoxingOfficeClient,
                                   OfficeSdkProperties officeSdkProperties) {
        this.chaoxingOfficeClient = chaoxingOfficeClient;
        this.officeSdkProperties = officeSdkProperties;
    }

    @Override
    public ProjectSubmitVO submitProjectApproval(ProjectCreateRequest request) {
        Integer approvalFormId = requiredProjectApproveFormId();
        Long submitUid = requiredSubmitUid();
        FormsData formsData = buildProjectFormsData(request);

        ApiModifyResponse response = chaoxingOfficeClient.saveApproveData(approvalFormId, submitUid, formsData);
        return ChaoxingResponseUtils.toProjectSubmitVO(response);
    }

    private FormsData buildProjectFormsData(ProjectCreateRequest request) {
        OfficeSdkProperties.Field field = officeSdkProperties.getField();
        FormsData formsData = new FormsData();
        formsData.addField(new EditinputField(field.getProjectCode(), buildProjectCode()));
        formsData.addField(new SelectBoxField(field.getCustomerId(), request.getCustomerId()));
        formsData.addField(new EditinputField(field.getCustomerName(), true));
        formsData.addField(new SelectBoxField(field.getCustomerLevel(), true));
        formsData.addField(new DateinputField(field.getProjectDate(), request.getProjectDate()));
        formsData.addField(new DateinputField(field.getExpectedFinishDate(), request.getExpectedFinishDate()));
        formsData.addField(buildProjectMembersField(field.getProjectMembers(), request));
        formsData.addField(buildQuoteDetailsField(field, request));
        formsData.addField(new NumberinputField(field.getTotalQuoteAmount(), true));
        formsData.addField(new SelectBoxField(field.getProjectStatus(), PROJECT_STATUS_INITIATED));
        return formsData;
    }

    private ContactField buildProjectMembersField(String alias, ProjectCreateRequest request) {
        ContactField contactField = new ContactField(alias);
        for (ContactDTO member : request.getProjectMembers()) {
            contactField.addValue(member.getUid(), member.getName());
        }
        return contactField;
    }

    private DetailField buildQuoteDetailsField(OfficeSdkProperties.Field field, ProjectCreateRequest request) {
        DetailField detailField = new DetailField(field.getQuoteDetails());
        for (QuoteDetailRequest quoteDetail : request.getQuoteDetails()) {
            FormsData rowData = new FormsData();
            rowData.addField(new EditinputField(field.getProductName(), quoteDetail.getProductName()));
            rowData.addField(new EditinputField(field.getSpecification(), quoteDetail.getSpecification()));
            rowData.addField(new NumberinputField(field.getQuantity(), quoteDetail.getQuantity().doubleValue()));
            rowData.addField(new NumberinputField(field.getUnitPrice(), quoteDetail.getUnitPrice().doubleValue()));
            rowData.addField(new NumberinputField(field.getSubtotal(), true));
            detailField.addSubField(rowData);
        }
        return detailField;
    }

    private String buildProjectCode() {
        return PROJECT_CODE_PREFIX + PROJECT_CODE_FORMATTER.format(LocalDateTime.now())
                + "-" + UUID.randomUUID();
    }

    private Integer requiredProjectApproveFormId() {
        return ConfigurationAssert.requireNonNull(
                officeSdkProperties.getForm().getProjectApproveFormId(),
                "chaoxing.office.form.project-approve-form-id"
        );
    }

    private Long requiredSubmitUid() {
        return ConfigurationAssert.requireNonNull(officeSdkProperties.getSubmitUid(), "chaoxing.office.submit-uid");
    }
}
