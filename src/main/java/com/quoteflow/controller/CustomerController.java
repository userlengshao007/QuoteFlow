package com.quoteflow.controller;

import com.quoteflow.dto.ApiResponse;
import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.FormSubmitVO;
import com.quoteflow.service.CustomerService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer REST controller.
 *
 * @author QuoteFlow
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    /**
     * Customer service.
     */
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Creates customer form data.
     *
     * @param request customer creation request
     * @return form submission response
     */
    @PostMapping
    public ApiResponse<FormSubmitVO> saveCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        return ApiResponse.success(customerService.saveCustomer(request));
    }
}
