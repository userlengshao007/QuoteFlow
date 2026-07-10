package com.quoteflow.service;

import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.CustomerInfoDTO;
import com.quoteflow.dto.FormSubmitVO;
import java.util.List;

/**
 * 人员信息服务。
 *
 * @author QuoteFlow
 */
public interface CustomerService {

    /**
     * 创建一条人员信息表数据。
     *
     * @param request 人员信息创建请求
     * @return 表单提交结果
     */
    FormSubmitVO saveCustomer(CustomerCreateRequest request);

    /**
     * 根据客户 ID 获取客户信息。
     *
     * @param customerId 客户 ID
     * @return 客户信息
     */
    CustomerInfoDTO getCustomerByCustomerId(String customerId);

    /**
     * 查询人员信息表中的客户列表。
     *
     * @return 客户列表
     */
    List<CustomerInfoDTO> listCustomers();
}
