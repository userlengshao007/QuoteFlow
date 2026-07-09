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
import com.quoteflow.dto.CustomerInfoDTO;
import com.quoteflow.dto.ProjectCreateRequest;
import com.quoteflow.dto.ProjectSubmitVO;
import com.quoteflow.dto.QuoteDetailRequest;
import com.quoteflow.service.CustomerService;
import com.quoteflow.service.ProjectQuoteService;
import com.quoteflow.util.ChaoxingResponseUtils;
import com.quoteflow.util.ConfigurationAssert;
import org.springframework.stereotype.Service;

/**
 * Project quote service implementation.
 *
 * @author QuoteFlow
 */
@Service
public class ProjectQuoteServiceImpl implements ProjectQuoteService {

    /**
     * Initial project status.
     */
    private static final String PROJECT_STATUS_INITIATED = "立项";

    /**
     * Customer service.
     */
    private final CustomerService customerService;

    /**
     * Chaoxing office client.
     */
    private final ChaoxingOfficeClient chaoxingOfficeClient;

    /**
     * Chaoxing SDK properties.
     */
    private final OfficeSdkProperties officeSdkProperties;

    public ProjectQuoteServiceImpl(CustomerService customerService,
                                   ChaoxingOfficeClient chaoxingOfficeClient,
                                   OfficeSdkProperties officeSdkProperties) {
        this.customerService = customerService;
        this.chaoxingOfficeClient = chaoxingOfficeClient;
        this.officeSdkProperties = officeSdkProperties;
    }

    @Override
    public ProjectSubmitVO submitProjectApproval(ProjectCreateRequest request) {
        Integer approvalFormId = requiredProjectApproveFormId();
        Long submitUid = requiredSubmitUid();
        CustomerInfoDTO customerInfo = customerService.getCustomerByCustomerId(request.getCustomerId());
        FormsData formsData = buildProjectFormsData(request, customerInfo);

        ApiModifyResponse response = chaoxingOfficeClient.saveApproveData(approvalFormId, submitUid, formsData);
        return ChaoxingResponseUtils.toProjectSubmitVO(response);
    }

    private FormsData buildProjectFormsData(ProjectCreateRequest request, CustomerInfoDTO customerInfo) {
        OfficeSdkProperties.Field field = officeSdkProperties.getField();
        FormsData formsData = new FormsData();
        formsData.addField(new EditinputField(field.getProjectCode(), true));
        formsData.addField(new EditinputField(field.getCustomerId(), request.getCustomerId()));
        formsData.addField(new EditinputField(field.getCustomerName(), customerInfo.getCustomerName()));
        formsData.addField(new SelectBoxField(field.getCustomerLevel(), customerInfo.getCustomerLevel()));
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
