package com.quoteflow.util;

import com.chaoxing.office.app.entity.forms.vo.data.ApiModifyResponseData;
import com.chaoxing.office.app.entity.forms.vo.data.SuccessFailedInfo;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.quoteflow.dto.FormSubmitVO;
import com.quoteflow.dto.ProjectSubmitVO;
import java.util.List;

/**
 * 超星 SDK 响应工具。
 *
 * @author QuoteFlow
 */
public final class ChaoxingResponseUtils {

    private ChaoxingResponseUtils() {
    }

    /**
     * 将超星表单保存响应转换为表单提交视图对象。
     *
     * @param response 超星响应
     * @return 表单提交视图对象
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
     * 将超星审批保存响应转换为项目提交视图对象。
     *
     * @param response 超星响应
     * @return 项目提交视图对象
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
