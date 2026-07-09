package com.quoteflow.client;

import com.chaoxing.modules.utils.OfficeApiException;
import com.chaoxing.office.app.entity.forms.dto.data.FormsData;
import com.chaoxing.office.app.entity.forms.dto.search.field.LogicSearchFilter;
import com.chaoxing.office.app.entity.forms.vo.response.ApiBaseResponse;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.chaoxing.office.app.entity.forms.vo.response.ApiSearchResponse;
import com.chaoxing.office.app.enums.SdkEnums.PushDataEnum;
import com.chaoxing.office.app.enums.SdkEnums.TriggerEnum;
import com.chaoxing.office.app.service.api.OfficeApproveApiInvokeService;
import com.chaoxing.office.app.service.api.OfficeFormsApiInvokeService;
import com.quoteflow.config.OfficeSdkProperties;
import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import com.quoteflow.util.ConfigurationAssert;
import org.springframework.stereotype.Component;

/**
 * 超星办公 SDK 客户端实现。
 *
 * @author QuoteFlow
 */
@Component
public class ChaoxingOfficeClientImpl implements ChaoxingOfficeClient {

    /**
     * 默认第一页。
     */
    private static final Integer DEFAULT_PAGE = 1;

    /**
     * 默认分页大小。
     */
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 是否在超星侧校验必填字段。
     */
    private static final Boolean CHECK_REQUIRED = Boolean.TRUE;

    /**
     * 是否自动过滤无效字段。
     */
    private static final Boolean AUTO_FILTER_ERROR_FIELDS = Boolean.FALSE;

    /**
     * 超星 SDK 配置。
     */
    private final OfficeSdkProperties officeSdkProperties;

    public ChaoxingOfficeClientImpl(OfficeSdkProperties officeSdkProperties) {
        this.officeSdkProperties = officeSdkProperties;
    }

    @Override
    public ApiModifyResponse saveFormData(Integer formId, Long submitUid, String uuid, FormsData formsData) {
        validateFormsCredential();
        Integer fid = requiredFid();
        try {
            ApiModifyResponse response = OfficeFormsApiInvokeService.saveCustomFormsData(
                    fid,
                    formId,
                    null,
                    submitUid,
                    uuid,
                    CHECK_REQUIRED,
                    PushDataEnum.TRUE.type,
                    formsData,
                    TriggerEnum.TRUE.type,
                    null,
                    null,
                    AUTO_FILTER_ERROR_FIELDS
            );
            assertSuccess(response);
            return response;
        } catch (OfficeApiException exception) {
            throw new BusinessException(ErrorCodeEnum.CHAOXING_SDK_ERROR, "新增表单数据失败", exception);
        }
    }

    @Override
    public ApiSearchResponse searchFormData(Integer formId,
                                            String returnFields,
                                            LogicSearchFilter logicSearchFilter,
                                            String formUserIds,
                                            Integer cpage,
                                            Integer pageSize) {
        validateFormsCredential();
        Integer fid = requiredFid();
        try {
            ApiSearchResponse response = OfficeFormsApiInvokeService.formUserAdvanceSearchListByFormId(
                    fid,
                    formId,
                    null,
                    null,
                    null,
                    returnFields,
                    logicSearchFilter,
                    "inserttime",
                    "desc",
                    null,
                    null,
                    null,
                    null,
                    formUserIds,
                    cpage == null ? DEFAULT_PAGE : cpage,
                    pageSize == null ? DEFAULT_PAGE_SIZE : pageSize
            );
            assertSuccess(response);
            return response;
        } catch (OfficeApiException exception) {
            throw new BusinessException(ErrorCodeEnum.CHAOXING_SDK_ERROR, "检索表单数据失败", exception);
        }
    }

    @Override
    public ApiModifyResponse saveApproveData(Integer approvalFormId, Long submitUid, FormsData formsData) {
        validateApproveCredential();
        Integer fid = requiredFid();
        try {
            ApiModifyResponse response = OfficeApproveApiInvokeService.saveApproveFormsData(
                    fid,
                    approvalFormId,
                    null,
                    submitUid,
                    CHECK_REQUIRED,
                    formsData,
                    null,
                    null,
                    null,
                    null,
                    null,
                    PushDataEnum.TRUE.type,
                    null,
                    AUTO_FILTER_ERROR_FIELDS
            );
            assertSuccess(response);
            return response;
        } catch (OfficeApiException exception) {
            throw new BusinessException(ErrorCodeEnum.CHAOXING_SDK_ERROR, "发起审批数据失败", exception);
        }
    }

    @Override
    public ApiSearchResponse searchApproveData(Integer approvalFormId,
                                               String returnFields,
                                               LogicSearchFilter logicSearchFilter,
                                               Integer cpage,
                                               Integer pageSize) {
        validateApproveCredential();
        Integer fid = requiredFid();
        try {
            ApiSearchResponse response = OfficeApproveApiInvokeService.approveUserAdvanceSearchListByFormId(
                    fid,
                    approvalFormId,
                    null,
                    null,
                    null,
                    null,
                    returnFields,
                    logicSearchFilter,
                    "inserttime",
                    "desc",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    cpage == null ? DEFAULT_PAGE : cpage,
                    pageSize == null ? DEFAULT_PAGE_SIZE : pageSize
            );
            assertSuccess(response);
            return response;
        } catch (OfficeApiException exception) {
            throw new BusinessException(ErrorCodeEnum.CHAOXING_SDK_ERROR, "检索审批数据失败", exception);
        }
    }

    private Integer requiredFid() {
        return ConfigurationAssert.requireNonNull(officeSdkProperties.getFid(), "chaoxing.office.fid");
    }

    private void validateFormsCredential() {
        ConfigurationAssert.requireNonBlank(officeSdkProperties.getFormsSign(), "chaoxing.office.forms-sign");
        ConfigurationAssert.requireNonBlank(officeSdkProperties.getFormsKey(), "chaoxing.office.forms-key");
    }

    private void validateApproveCredential() {
        ConfigurationAssert.requireNonBlank(officeSdkProperties.getApproveSign(), "chaoxing.office.approve-sign");
        ConfigurationAssert.requireNonBlank(officeSdkProperties.getApproveKey(), "chaoxing.office.approve-key");
    }

    private void assertSuccess(ApiBaseResponse response) {
        if (response == null || !Boolean.TRUE.equals(response.getSuccess())) {
            String message = response == null ? "超星接口无响应" : response.getMsg();
            throw new BusinessException(ErrorCodeEnum.CHAOXING_SDK_ERROR, message);
        }
    }
}
