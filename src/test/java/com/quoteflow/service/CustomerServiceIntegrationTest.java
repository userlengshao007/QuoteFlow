package com.quoteflow.service;

import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.FormSubmitVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 人员信息服务手动集成测试。
 *
 * @author QuoteFlow
 */
@SpringBootTest
@EnabledIfSystemProperty(named = "chaoxing.integration-test", matches = "true")
class CustomerServiceIntegrationTest {

    /**
     * 默认客户级别。
     */
    private static final String DEFAULT_CUSTOMER_LEVEL = "战略";

    /**
     * 默认所属行业。
     */
    private static final String DEFAULT_INDUSTRY = "信息技术";

    /**
     * 默认主要联系人 UID。
     */
    private static final Long DEFAULT_MAIN_CONTACT_ID = 199752933L;

    /**
     * 默认主要联系人姓名。
     */
    private static final String DEFAULT_MAIN_CONTACT_NAME = "章宇杰";

    /**
     * 人员信息服务。
     */
    @Autowired
    private CustomerService customerService;

    /**
     * 手动调用超星 SDK 新增一条人员信息表数据。
     */
    @Test
    void saveCustomerShouldCreateCustomerFormData() {
        CustomerCreateRequest request = buildCustomerCreateRequest();

        FormSubmitVO formSubmitVO = customerService.saveCustomer(request);

        Assertions.assertNotNull(formSubmitVO);
        Assertions.assertTrue(formSubmitVO.getFormUserId() != null || formSubmitVO.getRepeatFormUserId() != null);
    }

    private CustomerCreateRequest buildCustomerCreateRequest() {
        long currentTimeMillis = System.currentTimeMillis();
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setCustomerName("接口测试客户-" + currentTimeMillis);
        request.setCustomerLevel(System.getProperty("chaoxing.test.customer-level", DEFAULT_CUSTOMER_LEVEL));
        request.setMainContactId(Long.getLong("chaoxing.test.main-contact-id", DEFAULT_MAIN_CONTACT_ID));
        request.setMainContactName(System.getProperty("chaoxing.test.main-contact-name", DEFAULT_MAIN_CONTACT_NAME));
        request.setIndustry(System.getProperty("chaoxing.test.industry", DEFAULT_INDUSTRY));
        request.setUuid("customer-integration-test-" + currentTimeMillis);
        return request;
    }
}
