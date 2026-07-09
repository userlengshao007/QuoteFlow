package com.quoteflow.service.impl;

import com.chaoxing.office.app.entity.forms.dto.data.FormsData;
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
import com.quoteflow.dto.CustomerInfoDTO;
import com.quoteflow.dto.FormSubmitVO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Sales performance service implementation.
 *
 * @author QuoteFlow
 */
@Service
public class SalesPerformanceServiceImpl implements SalesPerformanceService {

    /**
     * Project query return fields.
     */
    private static final String PROJECT_RETURN_FIELDS =
            "[{\"alias\":\"%s\"},{\"alias\":\"%s\"},{\"alias\":\"%s\"},{\"alias\":\"%s\"}]";

    /**
     * Statistics id return fields.
     */
    private static final String STATISTICS_ID_RETURN_FIELDS = "[{\"alias\":\"%s\"}]";

    /**
     * Maximum project rows used in one statistics request.
     */
    private static final Integer MAX_PROJECT_PAGE_SIZE = 100;

    /**
     * Scale for money division.
     */
    private static final int MONEY_SCALE = 2;

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
        FormSubmitVO formSubmitVO = saveStatisticsForm(request, context, field);
        String statisticsId = queryStatisticsId(formSubmitVO.getFormUserId(), field);
        return toStatisticsVO(formSubmitVO, statisticsId, context);
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
                                            OfficeSdkProperties.Field field) {
        Integer salesStatisticsFormId = requiredSalesStatisticsFormId();
        Long submitUid = requiredSubmitUid();
        FormsData formsData = new FormsData();
        formsData.addField(new EditinputField(field.getStatisticsId(), true));
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

    private String queryStatisticsId(Long formUserId, OfficeSdkProperties.Field field) {
        if (formUserId == null) {
            return null;
        }
        ApiSearchResponse response = chaoxingOfficeClient.searchFormData(
                requiredSalesStatisticsFormId(),
                String.format(STATISTICS_ID_RETURN_FIELDS, field.getStatisticsId()),
                null,
                String.valueOf(formUserId),
                1,
                1
        );
        if (response.getData() == null || response.getData().getDataList() == null
                || response.getData().getDataList().isEmpty()) {
            return null;
        }
        return FormFieldValueUtils.getFirstText(response.getData().getDataList().get(0), field.getStatisticsId());
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

    private String buildSearchConditionText(SalesPerformanceStatisticsRequest request) {
        return "customerLevel=" + request.getCustomerLevel()
                + ", projectDateStart=" + request.getProjectDateStart()
                + ", projectDateEnd=" + request.getProjectDateEnd()
                + ", salesName=" + request.getSalesName();
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

    private Long requiredSubmitUid() {
        return ConfigurationAssert.requireNonNull(officeSdkProperties.getSubmitUid(), "chaoxing.office.submit-uid");
    }

    /**
     * Internal statistics accumulation context.
     *
     * @author QuoteFlow
     */
    private static class StatisticsContext {

        /**
         * Responsible customer id set.
         */
        private final Set<String> customerIdSet = new HashSet<>();

        /**
         * Project count.
         */
        private Integer projectCount = 0;

        /**
         * Total quote amount.
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
         * Calculates average project amount.
         *
         * @return average project amount
         */
        public BigDecimal calculateAverageProjectAmount() {
            if (projectCount == null || projectCount == 0) {
                return BigDecimal.ZERO;
            }
            return totalQuoteAmount.divide(BigDecimal.valueOf(projectCount), MONEY_SCALE, RoundingMode.HALF_UP);
        }
    }
}
