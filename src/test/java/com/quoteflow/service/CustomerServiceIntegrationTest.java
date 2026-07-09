package com.quoteflow.service;

import com.chaoxing.office.app.config.OfficeAppApiConfigTool;
import com.quoteflow.config.OfficeSdkProperties;
import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.FormSubmitVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 人员信息服务手动集成测试。
 *
 * @author QuoteFlow
 */
@SpringBootTest
class CustomerServiceIntegrationTest {

    /**
     * 表单测试 sign。
     */
    private static final String TEST_FORMS_SIGN = "appsFormsData_test";

    /**
     * 表单测试 key。
     */
    private static final String TEST_FORMS_KEY = "C&a%GKQRkIGtxhNdpa";

    /**
     * 单位 ID。当前还没有真实值。
     */
    private static final Integer TEST_FID = 176913;

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
     * 超星 SDK 配置。
     */
    @Autowired
    private OfficeSdkProperties officeSdkProperties;

    /**
     * 手动调用超星 SDK 新增一条人员信息表数据。
     */
    @Test
    void saveCustomerShouldCreateCustomerFormData() {
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
        request.setCustomerLevel(DEFAULT_CUSTOMER_LEVEL);
        request.setMainContactId(DEFAULT_MAIN_CONTACT_ID);
        request.setMainContactName(DEFAULT_MAIN_CONTACT_NAME);
        request.setIndustry(DEFAULT_INDUSTRY);
        request.setUuid("customer-integration-test-" + currentTimeMillis);
        return request;
    }
}
