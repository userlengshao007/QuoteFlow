package com.quoteflow.service;

import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.CustomerInfoDTO;
import com.quoteflow.dto.FormSubmitVO;

/**
 * Customer service.
 *
 * @author QuoteFlow
 */
public interface CustomerService {

    /**
     * Creates a customer form data record.
     *
     * @param request customer creation request
     * @return form submission result
     */
    FormSubmitVO saveCustomer(CustomerCreateRequest request);

    /**
     * Gets customer information by customer id.
     *
     * @param customerId customer id
     * @return customer information
     */
    CustomerInfoDTO getCustomerByCustomerId(String customerId);
}
