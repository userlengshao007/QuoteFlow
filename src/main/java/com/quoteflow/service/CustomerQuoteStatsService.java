package com.quoteflow.service;

import com.quoteflow.dto.CustomerQuoteStatsItemVO;
import com.quoteflow.dto.CustomerQuoteStatsRequest;
import java.util.List;

/**
 * 客户报价统计服务。
 *
 * @author QuoteFlow
 */
public interface CustomerQuoteStatsService {

    /**
     * 按客户汇总报价总额。
     *
     * @param request 客户报价统计请求
     * @return 统计明细列表
     */
    List<CustomerQuoteStatsItemVO> summarizeByCustomer(CustomerQuoteStatsRequest request);
}
