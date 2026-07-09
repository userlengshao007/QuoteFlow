package com.quoteflow.service;

import com.quoteflow.dto.CustomerQuoteStatsItemVO;
import com.quoteflow.dto.CustomerQuoteStatsRequest;
import java.util.List;

/**
 * Customer quote statistics service.
 *
 * @author QuoteFlow
 */
public interface CustomerQuoteStatsService {

    /**
     * Summarizes total quote amount by customer.
     *
     * @param request customer quote statistics request
     * @return statistics items
     */
    List<CustomerQuoteStatsItemVO> summarizeByCustomer(CustomerQuoteStatsRequest request);
}
