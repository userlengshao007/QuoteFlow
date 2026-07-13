package com.quoteflow.controller;

import com.quoteflow.dto.ApiResponse;
import com.quoteflow.dto.CustomerCreateRequest;
import com.quoteflow.dto.CustomerInfoDTO;
import com.quoteflow.dto.FormSubmitVO;
import com.quoteflow.service.CustomerService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员信息 REST 控制器。
 *
 * @author zhangyujie
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    /**
     * 人员信息服务。
     */
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 创建人员信息表单数据。
     *
     * @param request 人员信息创建请求
     * @return 表单提交响应
     */
    @PostMapping
    public ApiResponse<FormSubmitVO> saveCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        return ApiResponse.success(customerService.saveCustomer(request));
    }

    /**
     * 查询人员信息客户列表。
     *
     * @return 客户列表
     */
    @GetMapping
    public ApiResponse<List<CustomerInfoDTO>> listCustomers() {
        return ApiResponse.success(customerService.listCustomers());
    }
}
