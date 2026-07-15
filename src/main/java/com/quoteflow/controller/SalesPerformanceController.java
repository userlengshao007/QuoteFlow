package com.quoteflow.controller;

import com.quoteflow.dto.ApiResponse;
import com.quoteflow.dto.SalesPerformanceStatisticsRequest;
import com.quoteflow.dto.SalesPerformanceStatisticsVO;
import com.quoteflow.service.SalesPerformanceService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 销售业绩统计 REST 控制器。
 *
 * @author zhangyujie
 */
@RestController
@RequestMapping("/api/sales-performance")
public class SalesPerformanceController {

    /**
     * 销售业绩统计服务。
     */
    private final SalesPerformanceService salesPerformanceService;

    public SalesPerformanceController(SalesPerformanceService salesPerformanceService) {
        this.salesPerformanceService = salesPerformanceService;
    }

    /**
     * 创建销售业绩统计。
     *
     * @param request 统计请求
     * @return 统计响应
     */
    @PostMapping("/statistics")
    public ApiResponse<SalesPerformanceStatisticsVO> createStatistics(
            @Valid @RequestBody(required = false) SalesPerformanceStatisticsRequest request) {
        return ApiResponse.success(salesPerformanceService.createStatistics(request));
    }
}
