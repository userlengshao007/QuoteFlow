package com.quoteflow.service.impl;

import com.chaoxing.office.app.entity.forms.dto.data.FormsData;
import com.chaoxing.office.app.entity.forms.dto.data.field.ContactField;
import com.chaoxing.office.app.entity.forms.dto.data.field.EditinputField;
import com.chaoxing.office.app.entity.forms.dto.data.field.SelectBoxField;
import com.chaoxing.office.app.entity.forms.dto.search.field.BaseSearchFilter.SearchExpressTypeEnum;
import com.chaoxing.office.app.entity.forms.dto.search.field.BaseSearchFilter.SearchModelTypeEnum;
import com.chaoxing.office.app.entity.forms.dto.search.field.AutonumberSearchFilter;
import com.chaoxing.office.app.entity.forms.dto.search.field.LogicSearchFilter;
import com.chaoxing.office.app.entity.forms.vo.data.ApiFormUser;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.chaoxing.office.app.entity.forms.vo.response.ApiSearchResponse;
import com.quoteflow.client.ChaoxingOfficeClient;
import com.quoteflow.config.OfficeSdkProperties;
import com.quoteflow.dto.ContactDTO;
import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.CustomerInfoDTO;
import com.quoteflow.dto.FormSubmitVO;
import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import com.quoteflow.service.CustomerService;
import com.quoteflow.util.ChaoxingResponseUtils;
import com.quoteflow.util.ConfigurationAssert;
import com.quoteflow.util.FormFieldValueUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 人员信息服务实现。
 *
 * @author zhangyujie
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    /**
     * 客户查询返回字段。
     */
    private static final String CUSTOMER_RETURN_FIELDS =
            "[{\"alias\":\"%s\"},{\"alias\":\"%s\"},{\"alias\":\"%s\"},{\"alias\":\"%s\"}]";

    /**
     * 超星办公客户端。
     */
    private final ChaoxingOfficeClient chaoxingOfficeClient;

    /**
     * 超星 SDK 配置。
     */
    private final OfficeSdkProperties officeSdkProperties;

    public CustomerServiceImpl(ChaoxingOfficeClient chaoxingOfficeClient, OfficeSdkProperties officeSdkProperties) {
        this.chaoxingOfficeClient = chaoxingOfficeClient;
        this.officeSdkProperties = officeSdkProperties;
    }

    @Override
    public FormSubmitVO saveCustomer(CustomerCreateRequest request) {
        Integer customerFormId = requiredCustomerFormId();
        Long submitUid = requiredSubmitUid();
        OfficeSdkProperties.Field field = officeSdkProperties.getField();

        FormsData formsData = new FormsData();
        formsData.addField(new EditinputField(field.getCustomerName(), request.getCustomerName()));
        formsData.addField(new EditinputField(field.getCreditCode(), request.getCreditCode()));
        formsData.addField(new SelectBoxField(field.getCustomerLevel(), request.getCustomerLevel()));
        formsData.addField(new ContactField(field.getMainContact(), request.getMainContactId(),
                request.getMainContactName()));
        formsData.addField(new SelectBoxField(field.getIndustry(), request.getIndustry()));

        ApiModifyResponse response = chaoxingOfficeClient.saveFormData(
                customerFormId, submitUid, request.getUuid(), formsData);
        return ChaoxingResponseUtils.toFormSubmitVO(response);
    }

    @Override
    public CustomerInfoDTO getCustomerByCustomerId(String customerId) {
        Integer customerFormId = requiredCustomerFormId();
        OfficeSdkProperties.Field field = officeSdkProperties.getField();

        LogicSearchFilter filter = new LogicSearchFilter(SearchModelTypeEnum.AND);
        filter.addFilter(new AutonumberSearchFilter(field.getCustomerId(), SearchExpressTypeEnum.EQUALS, customerId));

        ApiSearchResponse response = chaoxingOfficeClient.searchFormData(
                customerFormId, buildCustomerReturnFields(field), filter, null, 1, 10);
        if (response.getData() == null || response.getData().getDataList() == null
                || response.getData().getDataList().isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.CUSTOMER_NOT_FOUND);
        }
        List<ApiFormUser> dataList = response.getData().getDataList();
        return toCustomerInfo(dataList.get(0), field);
    }

    @Override
    public List<CustomerInfoDTO> listCustomers() {
        Integer customerFormId = requiredCustomerFormId();
        OfficeSdkProperties.Field field = officeSdkProperties.getField();

        ApiSearchResponse response = chaoxingOfficeClient.searchFormData(
                customerFormId, buildCustomerReturnFields(field), null, null, 1, 100);
        List<CustomerInfoDTO> customerList = new ArrayList<>();
        if (response.getData() == null || response.getData().getDataList() == null) {
            return customerList;
        }
        for (ApiFormUser apiFormUser : response.getData().getDataList()) {
            customerList.add(toCustomerInfo(apiFormUser, field));
        }
        return customerList;
    }

    private CustomerInfoDTO toCustomerInfo(ApiFormUser apiFormUser, OfficeSdkProperties.Field field) {
        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO();
        customerInfoDTO.setCustomerId(FormFieldValueUtils.getFirstText(apiFormUser, field.getCustomerId()));
        customerInfoDTO.setCustomerName(FormFieldValueUtils.getFirstText(apiFormUser, field.getCustomerName()));
        customerInfoDTO.setCustomerLevel(FormFieldValueUtils.getFirstText(apiFormUser, field.getCustomerLevel()));

        ContactDTO mainContact = FormFieldValueUtils.getFirstContact(apiFormUser, field.getMainContact());
        customerInfoDTO.setMainContact(mainContact);
        return customerInfoDTO;
    }

    private String buildCustomerReturnFields(OfficeSdkProperties.Field field) {
        return String.format(CUSTOMER_RETURN_FIELDS,
                field.getCustomerId(), field.getCustomerName(), field.getCustomerLevel(), field.getMainContact());
    }

    private Integer requiredCustomerFormId() {
        return ConfigurationAssert.requireNonNull(
                officeSdkProperties.getForm().getCustomerFormId(),
                "chaoxing.office.form.customer-form-id"
        );
    }

    private Long requiredSubmitUid() {
        return ConfigurationAssert.requireNonNull(officeSdkProperties.getSubmitUid(), "chaoxing.office.submit-uid");
    }
}
