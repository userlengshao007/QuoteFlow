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
 * Customer quote statistics REST controller.
 *
 * @author QuoteFlow
 */
@RestController
@RequestMapping("/api/customer-quote-stats")
public class CustomerQuoteStatsController {

    /**
     * Customer quote statistics service.
     */
    private final CustomerQuoteStatsService customerQuoteStatsService;

    public CustomerQuoteStatsController(CustomerQuoteStatsService customerQuoteStatsService) {
        this.customerQuoteStatsService = customerQuoteStatsService;
    }

    /**
     * Summarizes selected projects by customer.
     *
     * @param request customer quote statistics request
     * @return customer quote statistics items
     */
    @PostMapping
    public ApiResponse<List<CustomerQuoteStatsItemVO>> summarizeByCustomer(
            @Valid @RequestBody CustomerQuoteStatsRequest request) {
        return ApiResponse.success(customerQuoteStatsService.summarizeByCustomer(request));
    }
}
