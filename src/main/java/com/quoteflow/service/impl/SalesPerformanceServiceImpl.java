package com.quoteflow.service.impl;

import com.chaoxing.office.app.entity.forms.dto.data.FormsData;
import com.chaoxing.office.app.entity.forms.dto.data.field.ContactField;
import com.chaoxing.office.app.entity.forms.dto.data.field.DateinputField;
import com.chaoxing.office.app.entity.forms.dto.data.field.EditinputField;
import com.chaoxing.office.app.entity.forms.dto.data.field.NumberinputField;
import com.chaoxing.office.app.entity.forms.dto.search.field.BaseSearchFilter.SearchExpressTypeEnum;
import com.chaoxing.office.app.entity.forms.dto.search.field.BaseSearchFilter.SearchModelTypeEnum;
import com.chaoxing.office.app.entity.forms.dto.search.field.DateinputSearchFilter;
import com.chaoxing.office.app.entity.forms.dto.search.field.LogicSearchFilter;
import com.chaoxing.office.app.entity.forms.dto.search.field.SelectBoxSearchFilter;
import com.chaoxing.office.app.entity.forms.vo.data.ApiFormUser;
import com.chaoxing.office.app.entity.forms.vo.response.ApiModifyResponse;
import com.chaoxing.office.app.entity.forms.vo.response.ApiSearchResponse;
import com.quoteflow.client.ChaoxingOfficeClient;
import com.quoteflow.config.OfficeSdkProperties;
import com.quoteflow.dto.ContactDTO;
import com.quoteflow.dto.CustomerInfoDTO;
import com.quoteflow.dto.FormSubmitVO;
import com.quoteflow.dto.ProjectApprovalPushResultVO;
import com.quoteflow.dto.SalesPerformanceStatisticsRequest;
import com.quoteflow.dto.SalesPerformanceStatisticsVO;
import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import com.quoteflow.service.CustomerService;
import com.quoteflow.service.SalesPerformanceService;
import com.quoteflow.util.ChaoxingResponseUtils;
import com.quoteflow.util.ConfigurationAssert;
import com.quoteflow.util.DateTimeUtils;
import com.quoteflow.util.FormFieldValueUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 销售业绩统计服务实现。
 *
 * @author QuoteFlow
 */
@Service
public class SalesPerformanceServiceImpl implements SalesPerformanceService {

    /**
     * 项目查询返回字段。
     */
    private static final String PROJECT_RETURN_FIELDS =
            "[{\"alias\":\"%s\"},{\"alias\":\"%s\"},{\"alias\":\"%s\"},{\"alias\":\"%s\"}]";

    /**
     * 项目审批回查返回字段。
     */
    private static final String PROJECT_PERFORMANCE_RETURN_FIELDS =
            "[{\"alias\":\"%s\"},{\"alias\":\"%s\"}]";

    /**
     * 单次统计请求使用的最大项目行数。
     */
    private static final Integer MAX_PROJECT_PAGE_SIZE = 100;

    /**
     * 审批最终通过状态 ID。
     */
    private static final Integer APPROVED_STATUS_ID = 1;

    /**
     * 审批最终通过状态名称。
     */
    private static final String APPROVED_STATUS_NAME = "已通过";

    /**
     * 推送 payload 中常见的项目审批数据 ID 字段名。
     */
    private static final String[] PROJECT_FORM_USER_ID_KEYS = {"formUserId", "form_user_id", "id"};

    /**
     * 业绩写入业务唯一 ID 前缀。
     */
    private static final String PERFORMANCE_UUID_PREFIX = "PROJECT_APPROVAL_PERFORMANCE_";

    /**
     * 金额除法保留位数。
     */
    private static final int MONEY_SCALE = 2;

    /**
     * 统计编号前缀。
     */
    private static final String STATISTICS_ID_PREFIX = "PERF-";

    /**
     * 统计编号年份格式。
     */
    private static final DateTimeFormatter STATISTICS_ID_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy");

    /**
     * 人员信息服务。
     */
    private final CustomerService customerService;

    /**
     * 超星办公客户端。
     */
    private final ChaoxingOfficeClient chaoxingOfficeClient;

    /**
     * 超星 SDK 配置。
     */
    private final OfficeSdkProperties officeSdkProperties;

    public SalesPerformanceServiceImpl(CustomerService customerService,
                                       ChaoxingOfficeClient chaoxingOfficeClient,
                                       OfficeSdkProperties officeSdkProperties) {
        this.customerService = customerService;
        this.chaoxingOfficeClient = chaoxingOfficeClient;
        this.officeSdkProperties = officeSdkProperties;
    }

    @Override
    public SalesPerformanceStatisticsVO createStatistics(SalesPerformanceStatisticsRequest request) {
        Integer projectApproveFormId = requiredProjectApproveFormId();
        OfficeSdkProperties.Field field = officeSdkProperties.getField();
        ApiSearchResponse projectResponse = chaoxingOfficeClient.searchApproveData(
                projectApproveFormId,
                buildProjectReturnFields(field),
                buildProjectSearchFilter(request, field),
                1,
                MAX_PROJECT_PAGE_SIZE
        );

        StatisticsContext context = calculateStatistics(projectResponse, request, field);
        String statisticsId = buildStatisticsId();
        FormSubmitVO formSubmitVO = saveStatisticsForm(request, context, field, statisticsId);
        return toStatisticsVO(formSubmitVO, statisticsId, context);
    }

    @Override
    public ProjectApprovalPushResultVO createPerformanceByApprovalPush(Map<String, Object> payload) {
        Long projectFormUserId = extractLong(payload, PROJECT_FORM_USER_ID_KEYS);
        if (projectFormUserId == null) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "项目审批数据 ID 不能为空");
        }

        ApiFormUser project = getProjectApproveData(projectFormUserId);
        if (!isApproved(project)) {
            return ignoredPushResult(project, "项目审批不是最终通过状态，已忽略");
        }

        OfficeSdkProperties.Field field = officeSdkProperties.getField();
        String customerId = FormFieldValueUtils.getFirstText(project, field.getCustomerId());
        if (!StringUtils.hasText(customerId)) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "项目客户 ID 不能为空");
        }

        CustomerInfoDTO customerInfo = customerService.getCustomerByCustomerId(customerId);
        ContactDTO salesContact = requiredMainContact(customerInfo);
        BigDecimal quoteTotal = FormFieldValueUtils.getFirstNumber(project, field.getTotalQuoteAmount());

        FormSubmitVO formSubmitVO = savePerformanceForm(project, salesContact, quoteTotal, field);
        return processedPushResult(project.getFormUserId(), formSubmitVO);
    }

    private ApiFormUser getProjectApproveData(Long projectFormUserId) {
        Integer projectApproveFormId = requiredProjectApproveFormId();
        OfficeSdkProperties.Field field = officeSdkProperties.getField();
        ApiSearchResponse response = chaoxingOfficeClient.getApproveDataByFormUserIds(
                projectApproveFormId,
                String.valueOf(projectFormUserId),
                buildProjectPerformanceReturnFields(field)
        );
        if (response.getData() == null || response.getData().getDataList() == null
                || response.getData().getDataList().isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.PROJECT_NOT_FOUND);
        }
        return response.getData().getDataList().get(0);
    }

    private boolean isApproved(ApiFormUser project) {
        return Objects.equals(APPROVED_STATUS_ID, project.getAprvStatusId())
                || APPROVED_STATUS_NAME.equals(project.getAprvStatus());
    }

    private ContactDTO requiredMainContact(CustomerInfoDTO customerInfo) {
        ContactDTO mainContact = customerInfo.getMainContact();
        if (mainContact == null || mainContact.getUid() == null || !StringUtils.hasText(mainContact.getName())) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "客户主要联系人未配置");
        }
        return mainContact;
    }

    private FormSubmitVO savePerformanceForm(ApiFormUser project,
                                             ContactDTO salesContact,
                                             BigDecimal quoteTotal,
                                             OfficeSdkProperties.Field field) {
        Integer salesPerformanceFormId = requiredSalesPerformanceFormId();
        Long submitUid = requiredSubmitUid();
        FormsData formsData = new FormsData();
        formsData.addField(new ContactField(field.getSalesContact(), salesContact.getUid(), salesContact.getName()));
        formsData.addField(new NumberinputField(field.getQuoteTotal(), quoteTotal.doubleValue()));

        String uuid = PERFORMANCE_UUID_PREFIX + project.getFormUserId();
        ApiModifyResponse response = chaoxingOfficeClient.saveFormData(salesPerformanceFormId, submitUid, uuid,
                formsData);
        return ChaoxingResponseUtils.toFormSubmitVO(response);
    }

    private ProjectApprovalPushResultVO ignoredPushResult(ApiFormUser project, String message) {
        ProjectApprovalPushResultVO resultVO = new ProjectApprovalPushResultVO();
        resultVO.setProcessed(Boolean.FALSE);
        resultVO.setMessage(message);
        resultVO.setProjectFormUserId(project.getFormUserId());
        return resultVO;
    }

    private ProjectApprovalPushResultVO processedPushResult(Long projectFormUserId, FormSubmitVO formSubmitVO) {
        ProjectApprovalPushResultVO resultVO = new ProjectApprovalPushResultVO();
        resultVO.setProcessed(Boolean.TRUE);
        resultVO.setMessage("销售业绩已生成");
        resultVO.setProjectFormUserId(projectFormUserId);
        resultVO.setPerformanceFormUserId(formSubmitVO.getFormUserId());
        resultVO.setRepeatPerformanceFormUserId(formSubmitVO.getRepeatFormUserId());
        return resultVO;
    }

    private Long extractLong(Object source, String... targetKeys) {
        Object value = findValue(source, targetKeys);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String && StringUtils.hasText((String) value)) {
            try {
                return Long.valueOf((String) value);
            } catch (NumberFormatException exception) {
                throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "项目审批数据 ID 格式不正确", exception);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object findValue(Object source, String... targetKeys) {
        if (source instanceof Map) {
            Map<String, Object> sourceMap = (Map<String, Object>) source;
            for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                if (matchesKey(entry.getKey(), targetKeys)) {
                    return entry.getValue();
                }
            }
            for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                Object value = findValue(entry.getValue(), targetKeys);
                if (value != null) {
                    return value;
                }
            }
        }
        if (source instanceof List) {
            for (Object item : (List<?>) source) {
                Object value = findValue(item, targetKeys);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    private boolean matchesKey(String sourceKey, String... targetKeys) {
        String normalizedSourceKey = normalizeKey(sourceKey);
        for (String targetKey : targetKeys) {
            if (normalizedSourceKey.equals(normalizeKey(targetKey))) {
                return true;
            }
        }
        return false;
    }

    private String normalizeKey(String key) {
        if (key == null) {
            return "";
        }
        return key.replace("_", "").replace("-", "").toLowerCase(Locale.ROOT);
    }

    private LogicSearchFilter buildProjectSearchFilter(SalesPerformanceStatisticsRequest request,
                                                       OfficeSdkProperties.Field field) {
        LogicSearchFilter filter = new LogicSearchFilter(SearchModelTypeEnum.AND);
        if (StringUtils.hasText(request.getCustomerLevel())) {
            filter.addFilter(new SelectBoxSearchFilter(
                    field.getCustomerLevel(), SearchExpressTypeEnum.EQUALS, request.getCustomerLevel()));
        }
        if (StringUtils.hasText(request.getProjectDateStart()) && StringUtils.hasText(request.getProjectDateEnd())) {
            filter.addFilter(new DateinputSearchFilter(
                    field.getProjectDate(),
                    SearchExpressTypeEnum.GREATER_THEN_EQUALS,
                    DateTimeUtils.toStartOfDayMillis(request.getProjectDateStart()),
                    SearchExpressTypeEnum.LESS_THAN_EQUALS,
                    DateTimeUtils.toEndOfDayMillis(request.getProjectDateEnd())
            ));
        }
        return filter;
    }

    private StatisticsContext calculateStatistics(ApiSearchResponse projectResponse,
                                                  SalesPerformanceStatisticsRequest request,
                                                  OfficeSdkProperties.Field field) {
        StatisticsContext context = new StatisticsContext();
        if (projectResponse.getData() == null || projectResponse.getData().getDataList() == null) {
            return context;
        }
        List<ApiFormUser> projectList = projectResponse.getData().getDataList();
        for (ApiFormUser project : projectList) {
            if (!matchesSalesName(project, request.getSalesName(), field)) {
                continue;
            }
            String customerId = FormFieldValueUtils.getFirstText(project, field.getCustomerId());
            if (StringUtils.hasText(customerId)) {
                context.getCustomerIdSet().add(customerId);
            }
            context.setProjectCount(context.getProjectCount() + 1);
            BigDecimal amount = FormFieldValueUtils.getFirstNumber(project, field.getTotalQuoteAmount());
            context.setTotalQuoteAmount(context.getTotalQuoteAmount().add(amount));
        }
        return context;
    }

    private boolean matchesSalesName(ApiFormUser project, String salesName, OfficeSdkProperties.Field field) {
        if (!StringUtils.hasText(salesName)) {
            return true;
        }
        String customerId = FormFieldValueUtils.getFirstText(project, field.getCustomerId());
        if (!StringUtils.hasText(customerId)) {
            return false;
        }
        try {
            CustomerInfoDTO customerInfo = customerService.getCustomerByCustomerId(customerId);
            return customerInfo.getMainContact() != null
                    && customerInfo.getMainContact().getName() != null
                    && customerInfo.getMainContact().getName().contains(salesName);
        } catch (BusinessException exception) {
            if (ErrorCodeEnum.CUSTOMER_NOT_FOUND.equals(exception.getErrorCodeEnum())) {
                return false;
            }
            throw exception;
        }
    }

    private FormSubmitVO saveStatisticsForm(SalesPerformanceStatisticsRequest request,
                                            StatisticsContext context,
                                            OfficeSdkProperties.Field field,
                                            String statisticsId) {
        Integer salesStatisticsFormId = requiredSalesStatisticsFormId();
        Long submitUid = requiredSubmitUid();
        FormsData formsData = new FormsData();
        formsData.addField(new EditinputField(field.getStatisticsId(), statisticsId));
        formsData.addField(new DateinputField(field.getStatisticsDate(), System.currentTimeMillis()));
        formsData.addField(new NumberinputField(field.getResponsibleCustomers(),
                Double.valueOf(context.getCustomerIdSet().size())));
        formsData.addField(new NumberinputField(field.getProjectCount(), Double.valueOf(context.getProjectCount())));
        formsData.addField(new NumberinputField(field.getTotalQuoteAmount(),
                context.getTotalQuoteAmount().doubleValue()));
        formsData.addField(new NumberinputField(field.getAverageProjectAmount(),
                context.calculateAverageProjectAmount().doubleValue()));
        formsData.addField(new EditinputField(field.getSearchCondition(), buildSearchConditionText(request)));

        ApiModifyResponse response = chaoxingOfficeClient.saveFormData(salesStatisticsFormId, submitUid, null,
                formsData);
        return ChaoxingResponseUtils.toFormSubmitVO(response);
    }

    private SalesPerformanceStatisticsVO toStatisticsVO(FormSubmitVO formSubmitVO,
                                                        String statisticsId,
                                                        StatisticsContext context) {
        SalesPerformanceStatisticsVO statisticsVO = new SalesPerformanceStatisticsVO();
        statisticsVO.setFormUserId(formSubmitVO.getFormUserId());
        statisticsVO.setStatisticsId(statisticsId);
        statisticsVO.setResponsibleCustomers(context.getCustomerIdSet().size());
        statisticsVO.setProjectCount(context.getProjectCount());
        statisticsVO.setTotalQuoteAmount(context.getTotalQuoteAmount());
        statisticsVO.setAverageProjectAmount(context.calculateAverageProjectAmount());
        return statisticsVO;
    }

    private String buildProjectReturnFields(OfficeSdkProperties.Field field) {
        return String.format(PROJECT_RETURN_FIELDS,
                field.getCustomerId(), field.getCustomerLevel(), field.getProjectDate(), field.getTotalQuoteAmount());
    }

    private String buildProjectPerformanceReturnFields(OfficeSdkProperties.Field field) {
        return String.format(PROJECT_PERFORMANCE_RETURN_FIELDS, field.getCustomerId(), field.getTotalQuoteAmount());
    }

    private String buildSearchConditionText(SalesPerformanceStatisticsRequest request) {
        return "customerLevel=" + request.getCustomerLevel()
                + ", projectDateStart=" + request.getProjectDateStart()
                + ", projectDateEnd=" + request.getProjectDateEnd()
                + ", salesName=" + request.getSalesName();
    }

    private String buildStatisticsId() {
        return STATISTICS_ID_PREFIX + STATISTICS_ID_FORMATTER.format(LocalDateTime.now())
                + "-" + UUID.randomUUID();
    }

    private Integer requiredProjectApproveFormId() {
        return ConfigurationAssert.requireNonNull(
                officeSdkProperties.getForm().getProjectApproveFormId(),
                "chaoxing.office.form.project-approve-form-id"
        );
    }

    private Integer requiredSalesStatisticsFormId() {
        return ConfigurationAssert.requireNonNull(
                officeSdkProperties.getForm().getSalesStatisticsFormId(),
                "chaoxing.office.form.sales-statistics-form-id"
        );
    }

    private Integer requiredSalesPerformanceFormId() {
        return ConfigurationAssert.requireNonNull(
                officeSdkProperties.getForm().getSalesPerformanceFormId(),
                "chaoxing.office.form.sales-performance-form-id"
        );
    }

    private Long requiredSubmitUid() {
        return ConfigurationAssert.requireNonNull(officeSdkProperties.getSubmitUid(), "chaoxing.office.submit-uid");
    }

    /**
     * 内部统计累加上下文。
     *
     * @author QuoteFlow
     */
    private static class StatisticsContext {

        /**
         * 负责客户 ID 集合。
         */
        private final Set<String> customerIdSet = new HashSet<>();

        /**
         * 项目数量。
         */
        private Integer projectCount = 0;

        /**
         * 报价总额。
         */
        private BigDecimal totalQuoteAmount = BigDecimal.ZERO;

        public Set<String> getCustomerIdSet() {
            return customerIdSet;
        }

        public Integer getProjectCount() {
            return projectCount;
        }

        public void setProjectCount(Integer projectCount) {
            this.projectCount = projectCount;
        }

        public BigDecimal getTotalQuoteAmount() {
            return totalQuoteAmount;
        }

        public void setTotalQuoteAmount(BigDecimal totalQuoteAmount) {
            this.totalQuoteAmount = totalQuoteAmount;
        }

        /**
         * 计算平均项目金额。
         *
         * @return 平均项目金额
         */
        public BigDecimal calculateAverageProjectAmount() {
            if (projectCount == null || projectCount == 0) {
                return BigDecimal.ZERO;
            }
            return totalQuoteAmount.divide(BigDecimal.valueOf(projectCount), MONEY_SCALE, RoundingMode.HALF_UP);
        }
    }
}
