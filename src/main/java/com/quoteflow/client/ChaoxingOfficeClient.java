package com.quoteflow.client;

import com.chaoxing.office.app.entity.forms.dto.data.FormsData;
import com.chaoxing.office.app.entity.forms.dto.search.field.LogicSearchFilter;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.chaoxing.office.app.entity.forms.vo.response.ApiSearchResponse;

/**
 * 超星办公 SDK 客户端门面。
 *
 * @author zhangyujie
 */
public interface ChaoxingOfficeClient {

    /**
     * 保存一条普通表单数据。
     *
     * @param formId 表单 ID
     * @param submitUid 提交人 UID
     * @param uuid 调用方业务唯一 ID
     * @param formsData 表单数据
     * @return 超星变更响应
     */
    ApiModifyResponse saveFormData(Integer formId, Long submitUid, String uuid, FormsData formsData);

    /**
     * 检索普通表单数据。
     *
     * @param formId 表单 ID
     * @param returnFields 返回字段 JSON
     * @param logicSearchFilter 逻辑检索条件
     * @param formUserIds 表单数据 ID 列表，多个用英文逗号分隔
     * @param cpage 页码
     * @param pageSize 每页条数
     * @return 超星检索响应
     */
    ApiSearchResponse searchFormData(Integer formId,
                                     String returnFields,
                                     LogicSearchFilter logicSearchFilter,
                                     String formUserIds,
                                     Integer cpage,
                                     Integer pageSize);

    /**
     * 提交一条审批数据。
     *
     * @param approvalFormId 审批表单 ID
     * @param submitUid 提交人 UID
     * @param formsData 表单数据
     * @return 超星变更响应
     */
    ApiModifyResponse saveApproveData(Integer approvalFormId, Long submitUid, FormsData formsData);

    /**
     * 检索审批数据。
     *
     * @param approvalFormId 审批表单 ID
     * @param returnFields 返回字段 JSON
     * @param logicSearchFilter 逻辑检索条件
     * @param cpage 页码
     * @param pageSize 每页条数
     * @return 超星检索响应
     */
    ApiSearchResponse searchApproveData(Integer approvalFormId,
                                        String returnFields,
                                        LogicSearchFilter logicSearchFilter,
                                        Integer cpage,
                                        Integer pageSize);

    /**
     * 根据审批数据 ID 检索审批数据。
     *
     * @param approvalFormId 审批表单 ID
     * @param formUserIds 审批数据 ID 列表，多个用英文逗号分隔
     * @param returnFields 返回字段 JSON
     * @return 超星检索响应
     */
    ApiSearchResponse getApproveDataByFormUserIds(Integer approvalFormId, String formUserIds, String returnFields);

    /**
     * 根据顶部按钮 queryId 检索审批选中数据。
     *
     * @param uid 点击顶部按钮的用户 UID
     * @param queryId 顶部按钮回传的查询条件 ID
     * @param returnFields 返回字段 JSON
     * @param limit 每次查询数量
     * @param sortValues 游标
     * @return 超星检索响应
     */
    ApiSearchResponse searchApproveTopDataByQueryId(Long uid,
                                                    String queryId,
                                                    String returnFields,
                                                    Integer limit,
                                                    String sortValues);
}
