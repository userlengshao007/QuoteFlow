package com.quoteflow.service;

import com.quoteflow.dto.SalesPerformanceStatisticsRequest;
import com.quoteflow.dto.SalesPerformanceStatisticsVO;

/**
 * Sales performance service.
 *
 * @author QuoteFlow
 */
public interface SalesPerformanceService {

    /**
     * Creates sales performance statistics.
     *
     * @param request statistics request
     * @return statistics result
     */
    SalesPerformanceStatisticsVO createStatistics(SalesPerformanceStatisticsRequest request);
}
