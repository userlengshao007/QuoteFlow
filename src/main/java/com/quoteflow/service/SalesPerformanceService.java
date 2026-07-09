package com.quoteflow.service;

import com.quoteflow.dto.SalesPerformanceStatisticsRequest;
import com.quoteflow.dto.SalesPerformanceStatisticsVO;

/**
 * 销售业绩统计服务。
 *
 * @author QuoteFlow
 */
public interface SalesPerformanceService {

    /**
     * 创建销售业绩统计。
     *
     * @param request 统计请求
     * @return 统计结果
     */
    SalesPerformanceStatisticsVO createStatistics(SalesPerformanceStatisticsRequest request);
}
