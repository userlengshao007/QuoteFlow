package com.quoteflow.service.impl;

import com.chaoxing.office.app.entity.forms.vo.data.ApiFormUser;
import com.chaoxing.office.app.entity.forms.vo.response.ApiSearchResponse;
import com.quoteflow.client.ChaoxingOfficeClient;
import com.quoteflow.config.OfficeSdkProperties;
import com.quoteflow.dto.CustomerQuoteStatsItemVO;
import com.quoteflow.dto.CustomerQuoteStatsRequest;
import com.quoteflow.service.CustomerQuoteStatsService;
import com.quoteflow.util.ConfigurationAssert;
import com.quoteflow.util.FormFieldValueUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 客户报价统计服务实现。
 *
 * @author zhangyujie
 */
@Service
public class CustomerQuoteStatsServiceImpl implements CustomerQuoteStatsService {

    /**
     * 日志记录器。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerQuoteStatsServiceImpl.class);

    /**
     * 客户报价统计返回字段。
     */
    private static final String PROJECT_RETURN_FIELDS =
            "[{\"alias\":\"%s\"},{\"alias\":\"%s\"},{\"alias\":\"%s\"}]";

    /**
     * 单次 SDK 请求的最大选中数据量。
     */
    private static final Integer MAX_SELECTED_SIZE = 100;

    /**
     * 顶部按钮游标查询最大次数。
     */
    private static final Integer MAX_TOP_SEARCH_COUNT = 100;

    /**
     * 超星办公客户端。
     */
    private final ChaoxingOfficeClient chaoxingOfficeClient;

    /**
     * 超星 SDK 配置。
     */
    private final OfficeSdkProperties officeSdkProperties;

    public CustomerQuoteStatsServiceImpl(ChaoxingOfficeClient chaoxingOfficeClient,
                                         OfficeSdkProperties officeSdkProperties) {
        this.chaoxingOfficeClient = chaoxingOfficeClient;
        this.officeSdkProperties = officeSdkProperties;
    }

    @Override
    public List<CustomerQuoteStatsItemVO> summarizeByCustomer(CustomerQuoteStatsRequest request) {
        Integer projectApproveFormId = requiredProjectApproveFormId();
        OfficeSdkProperties.Field field = officeSdkProperties.getField();
        List<ApiFormUser> selectedProjects = listProjectsByFormUserIds(
                projectApproveFormId, buildProjectReturnFields(field), request.getFormUserIds());
        return groupByCustomer(selectedProjects, field);
    }

    @Override
    public List<CustomerQuoteStatsItemVO> summarizeTopSelected(Long uid, String queryId) {
        ConfigurationAssert.requireNonNull(uid, "uid");
        ConfigurationAssert.requireNonBlank(queryId, "queryId");

        OfficeSdkProperties.Field field = officeSdkProperties.getField();
        List<ApiFormUser> selectedProjects = listTopSelectedProjects(uid, queryId, buildProjectReturnFields(field));
        return groupByCustomer(selectedProjects, field);
    }

    private List<ApiFormUser> listTopSelectedProjects(Long uid, String queryId, String returnFields) {
        List<ApiFormUser> selectedProjects = new ArrayList<>();
        String sortValues = null;
        for (int searchCount = 1; searchCount <= MAX_TOP_SEARCH_COUNT; searchCount++) {
            ApiSearchResponse response = chaoxingOfficeClient.searchApproveTopDataByQueryId(
                    uid,
                    queryId,
                    returnFields,
                    MAX_SELECTED_SIZE,
                    sortValues
            );
            if (response.getData() == null) {
                return selectedProjects;
            }

            List<ApiFormUser> dataList = response.getData().getDataList();
            if (dataList != null) {
                selectedProjects.addAll(dataList);
            }

            String nextSortValues = response.getData().getSortValues();
            Integer total = response.getData().getTotal();
            if (!StringUtils.hasText(nextSortValues)
                    || Objects.equals(sortValues, nextSortValues)
                    || (total != null && selectedProjects.size() >= total)) {
                return selectedProjects;
            }
            sortValues = nextSortValues;
        }
        LOGGER.warn("顶部按钮选中数据游标查询超过最大次数，uid={}, queryId={}", uid, queryId);
        return selectedProjects;
    }

    private List<ApiFormUser> listProjectsByFormUserIds(Integer projectApproveFormId,
                                                        String returnFields,
                                                        String formUserIds) {
        List<ApiFormUser> selectedProjects = new ArrayList<>();
        List<String> idList = splitFormUserIds(formUserIds);
        for (int startIndex = 0; startIndex < idList.size(); startIndex += MAX_SELECTED_SIZE) {
            String chunkIds = joinIds(idList, startIndex, Math.min(startIndex + MAX_SELECTED_SIZE, idList.size()));
            ApiSearchResponse response = chaoxingOfficeClient.getApproveDataByFormUserIds(
                    projectApproveFormId,
                    chunkIds,
                    returnFields
            );
            if (response.getData() != null && response.getData().getDataList() != null) {
                selectedProjects.addAll(response.getData().getDataList());
            }
        }
        return selectedProjects;
    }

    private List<String> splitFormUserIds(String formUserIds) {
        List<String> idList = new ArrayList<>();
        if (!StringUtils.hasText(formUserIds)) {
            return idList;
        }
        String[] ids = formUserIds.split(",");
        for (String id : ids) {
            if (StringUtils.hasText(id)) {
                idList.add(id.trim());
            }
        }
        return idList;
    }

    private String joinIds(List<String> idList, int startIndex, int endIndex) {
        StringBuilder builder = new StringBuilder();
        for (int index = startIndex; index < endIndex; index++) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(idList.get(index));
        }
        return builder.toString();
    }

    private List<CustomerQuoteStatsItemVO> groupByCustomer(List<ApiFormUser> selectedProjects,
                                                           OfficeSdkProperties.Field field) {
        Map<String, CustomerQuoteStatsItemVO> itemMap = new LinkedHashMap<>();
        if (selectedProjects == null) {
            return new ArrayList<>();
        }
        for (ApiFormUser project : selectedProjects) {
            String customerId = FormFieldValueUtils.getFirstText(project, field.getCustomerId());
            CustomerQuoteStatsItemVO item = itemMap.computeIfAbsent(customerId, key -> newStatsItem(project, field));
            item.setProjectCount(item.getProjectCount() + 1);
            BigDecimal amount = FormFieldValueUtils.getFirstNumber(project, field.getTotalQuoteAmount());
            item.setTotalQuoteAmount(item.getTotalQuoteAmount().add(amount));
        }
        return new ArrayList<>(itemMap.values());
    }

    private CustomerQuoteStatsItemVO newStatsItem(ApiFormUser project, OfficeSdkProperties.Field field) {
        CustomerQuoteStatsItemVO item = new CustomerQuoteStatsItemVO();
        item.setCustomerId(FormFieldValueUtils.getFirstText(project, field.getCustomerId()));
        item.setCustomerName(FormFieldValueUtils.getFirstText(project, field.getCustomerName()));
        item.setProjectCount(0);
        item.setTotalQuoteAmount(BigDecimal.ZERO);
        return item;
    }

    private String buildProjectReturnFields(OfficeSdkProperties.Field field) {
        return String.format(PROJECT_RETURN_FIELDS,
                field.getCustomerId(), field.getCustomerName(), field.getTotalQuoteAmount());
    }

    private Integer requiredProjectApproveFormId() {
        return ConfigurationAssert.requireNonNull(
                officeSdkProperties.getForm().getProjectApproveFormId(),
                "chaoxing.office.form.project-approve-form-id"
        );
    }
}
