package com.quoteflow.util;

import com.chaoxing.office.app.entity.forms.vo.data.ApiModifyResponseData;
import com.chaoxing.office.app.entity.forms.vo.data.SuccessFailedInfo;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.quoteflow.dto.FormSubmitVO;
import com.quoteflow.dto.ProjectSubmitVO;
import java.util.List;

/**
 * Chaoxing SDK response utility.
 *
 * @author QuoteFlow
 */
public final class ChaoxingResponseUtils {

    private ChaoxingResponseUtils() {
    }

    /**
     * Converts Chaoxing form save response to form submission view object.
     *
     * @param response Chaoxing response
     * @return form submission view object
     */
    public static FormSubmitVO toFormSubmitVO(ApiModifyResponse response) {
        ApiModifyResponseData data = response.getData();
        FormSubmitVO formSubmitVO = new FormSubmitVO();
        if (data == null) {
            return formSubmitVO;
        }
        formSubmitVO.setFormUserId(resolveFormUserId(data));
        formSubmitVO.setRepeatFormUserId(data.getRepeatFormUserId());
        return formSubmitVO;
    }

    /**
     * Converts Chaoxing approval save response to project submission view object.
     *
     * @param response Chaoxing response
     * @return project submission view object
     */
    public static ProjectSubmitVO toProjectSubmitVO(ApiModifyResponse response) {
        ApiModifyResponseData data = response.getData();
        ProjectSubmitVO projectSubmitVO = new ProjectSubmitVO();
        if (data == null) {
            return projectSubmitVO;
        }
        projectSubmitVO.setFormUserId(resolveFormUserId(data));
        projectSubmitVO.setDetailUrl(data.getDetailUrl());
        return projectSubmitVO;
    }

    private static Long resolveFormUserId(ApiModifyResponseData data) {
        if (data.getFormUserId() != null) {
            return data.getFormUserId();
        }
        List<SuccessFailedInfo> succeedList = data.getSucceedList();
        if (succeedList == null || succeedList.isEmpty()) {
            return null;
        }
        return succeedList.get(0).getFormUserId();
    }
}
