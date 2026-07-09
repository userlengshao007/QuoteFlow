package com.quoteflow.controller;

import com.quoteflow.dto.ApiResponse;
import com.quoteflow.dto.CustomerQuoteStatsItemVO;
import com.quoteflow.dto.CustomerQuoteStatsRequest;
import com.quoteflow.service.CustomerQuoteStatsService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户报价统计 REST 控制器。
 *
 * @author QuoteFlow
 */
@RestController
@RequestMapping("/api/customer-quote-stats")
public class CustomerQuoteStatsController {

    /**
     * 客户报价统计服务。
     */
    private final CustomerQuoteStatsService customerQuoteStatsService;

    public CustomerQuoteStatsController(CustomerQuoteStatsService customerQuoteStatsService) {
        this.customerQuoteStatsService = customerQuoteStatsService;
    }

    /**
     * 按客户汇总选中的项目数据。
     *
     * @param request 客户报价统计请求
     * @return 客户报价统计明细列表
     */
    @PostMapping
    public ApiResponse<List<CustomerQuoteStatsItemVO>> summarizeByCustomer(
            @Valid @RequestBody CustomerQuoteStatsRequest request) {
        return ApiResponse.success(customerQuoteStatsService.summarizeByCustomer(request));
    }
}
