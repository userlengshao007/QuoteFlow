package com.quoteflow.service;

import com.chaoxing.office.app.config.OfficeAppApiConfigTool;
import com.quoteflow.config.OfficeSdkProperties;
import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.FormSubmitVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

/**
 * 人员信息服务手动集成测试。
 *
 * @author zhangyujie
 */
@SpringBootTest
class CustomerServiceIntegrationTest {

    /**
     * 表单测试 sign。
     */
    private static final String TEST_FORMS_SIGN = getTestProperty(
            "chaoxing.test.forms-sign", "CHAOXING_FORMS_SIGN");

    /**
     * 表单测试 key。
     */
    private static final String TEST_FORMS_KEY = getTestProperty(
            "chaoxing.test.forms-key", "CHAOXING_FORMS_KEY");

    /**
     * 单位 ID。当前还没有真实值。
     */
    private static final Integer TEST_FID = 176913;

    /**
     * 默认客户级别。
     */
    private static final String DEFAULT_CUSTOMER_LEVEL = "普通";

    /**
     * 默认所属行业。
     */
    private static final String DEFAULT_INDUSTRY = "医疗健康";

    /**
     * 默认主要联系人 UID。
     */
    private static final Long DEFAULT_MAIN_CONTACT_ID = 233L;

    /**
     * 默认主要联系人姓名。
     */
    private static final String DEFAULT_MAIN_CONTACT_NAME = "李四";

    /**
     * 人员信息服务。
     */
    @Autowired
    private CustomerService customerService;

    /**
     * 超星 SDK 配置。
     */
    @Autowired
    private OfficeSdkProperties officeSdkProperties;

    /**
     * 手动调用超星 SDK 新增一条人员信息表数据。
     */
    @Test
    void saveCustomerShouldCreateCustomerFormData() {
        Assumptions.assumeTrue(StringUtils.hasText(TEST_FORMS_SIGN), "未配置 CHAOXING_FORMS_SIGN，跳过真实新增测试");
        Assumptions.assumeTrue(StringUtils.hasText(TEST_FORMS_KEY), "未配置 CHAOXING_FORMS_KEY，跳过真实新增测试");
        Assertions.assertTrue(TEST_FID > 0, "请先把 TEST_FID 改成真实单位 ID，再运行真实新增测试");
        CustomerCreateRequest request = buildCustomerCreateRequest();

        FormSubmitVO formSubmitVO = customerService.saveCustomer(request);

        Assertions.assertNotNull(formSubmitVO);
        Assertions.assertTrue(formSubmitVO.getFormUserId() != null || formSubmitVO.getRepeatFormUserId() != null);
    }

    @BeforeEach
    void setUpChaoxingTestProperties() {
        officeSdkProperties.setFormsSign(TEST_FORMS_SIGN);
        officeSdkProperties.setFormsKey(TEST_FORMS_KEY);
        officeSdkProperties.setFid(TEST_FID);
        OfficeAppApiConfigTool.officeApiFormSign = TEST_FORMS_SIGN;
        OfficeAppApiConfigTool.officeApiFormKey = TEST_FORMS_KEY;
    }

    private CustomerCreateRequest buildCustomerCreateRequest() {
        long currentTimeMillis = System.currentTimeMillis();
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setCustomerName("接口测试客户-" + currentTimeMillis);
        request.setCreditCode("91310000" + String.valueOf(currentTimeMillis).substring(5, 15));
        request.setCustomerLevel(DEFAULT_CUSTOMER_LEVEL);
        request.setMainContactId(DEFAULT_MAIN_CONTACT_ID);
        request.setMainContactName(DEFAULT_MAIN_CONTACT_NAME);
        request.setIndustry(DEFAULT_INDUSTRY);
        request.setUuid("customer-integration-test-" + currentTimeMillis);
        return request;
    }

    private static String getTestProperty(String propertyName, String environmentName) {
        String propertyValue = System.getProperty(propertyName);
        if (StringUtils.hasText(propertyValue)) {
            return propertyValue;
        }
        return System.getenv(environmentName);
    }
}
