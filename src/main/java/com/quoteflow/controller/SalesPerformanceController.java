package com.quoteflow.controller;

import com.quoteflow.dto.ApiResponse;
import com.quoteflow.dto.SalesPerformanceStatisticsRequest;
import com.quoteflow.dto.SalesPerformanceStatisticsVO;
import com.quoteflow.service.SalesPerformanceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sales performance REST controller.
 *
 * @author QuoteFlow
 */
@RestController
@RequestMapping("/api/sales-performance")
public class SalesPerformanceController {

    /**
     * Sales performance service.
     */
    private final SalesPerformanceService salesPerformanceService;

    public SalesPerformanceController(SalesPerformanceService salesPerformanceService) {
        this.salesPerformanceService = salesPerformanceService;
    }

    /**
     * Creates sales performance statistics.
     *
     * @param request statistics request
     * @return statistics response
     */
    @PostMapping("/statistics")
    public ApiResponse<SalesPerformanceStatisticsVO> createStatistics(
            @RequestBody SalesPerformanceStatisticsRequest request) {
        return ApiResponse.success(salesPerformanceService.createStatistics(request));
    }
}
