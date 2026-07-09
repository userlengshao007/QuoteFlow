package com.quoteflow.client;

import com.chaoxing.office.app.entity.forms.dto.data.FormsData;
import com.chaoxing.office.app.entity.forms.dto.search.field.LogicSearchFilter;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.chaoxing.office.app.entity.forms.vo.response.ApiSearchResponse;

/**
 * Chaoxing office SDK client facade.
 *
 * @author QuoteFlow
 */
public interface ChaoxingOfficeClient {

    /**
     * Saves one normal form data record.
     *
     * @param formId form id
     * @param submitUid submitter uid
     * @param uuid caller business unique id
     * @param formsData form data
     * @return Chaoxing modify response
     */
    ApiModifyResponse saveFormData(Integer formId, Long submitUid, String uuid, FormsData formsData);

    /**
     * Searches normal form data.
     *
     * @param formId form id
     * @param returnFields return field json
     * @param logicSearchFilter logic search filter
     * @param formUserIds form data id list, separated by comma
     * @param cpage page number
     * @param pageSize page size
     * @return Chaoxing search response
     */
    ApiSearchResponse searchFormData(Integer formId,
                                     String returnFields,
                                     LogicSearchFilter logicSearchFilter,
                                     String formUserIds,
                                     Integer cpage,
                                     Integer pageSize);

    /**
     * Submits one approval data record.
     *
     * @param approvalFormId approval form id
     * @param submitUid submitter uid
     * @param formsData form data
     * @return Chaoxing modify response
     */
    ApiModifyResponse saveApproveData(Integer approvalFormId, Long submitUid, FormsData formsData);

    /**
     * Searches approval data.
     *
     * @param approvalFormId approval form id
     * @param returnFields return field json
     * @param logicSearchFilter logic search filter
     * @param cpage page number
     * @param pageSize page size
     * @return Chaoxing search response
     */
    ApiSearchResponse searchApproveData(Integer approvalFormId,
                                        String returnFields,
                                        LogicSearchFilter logicSearchFilter,
                                        Integer cpage,
                                        Integer pageSize);
}
