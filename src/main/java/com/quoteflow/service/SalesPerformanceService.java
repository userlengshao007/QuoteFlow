package com.quoteflow.service;

import com.quoteflow.dto.ProjectApprovalPushResultVO;
import com.quoteflow.dto.SalesPerformanceStatisticsRequest;
import com.quoteflow.dto.SalesPerformanceStatisticsVO;
import java.util.Map;

/**
 * 销售业绩统计服务。
 *
 * @author zhangyujie
 */
public interface SalesPerformanceService {

    /**
     * 创建销售业绩统计。
     *
     * @param request 统计请求
     * @return 统计结果
     */
    SalesPerformanceStatisticsVO createStatistics(SalesPerformanceStatisticsRequest request);

    /**
     * 根据项目审批数据推送生成销售业绩。
     *
     * @param payload 数据推送原始参数
     * @return 推送处理结果
     */
    ProjectApprovalPushResultVO createPerformanceByApprovalPush(Map<String, Object> payload);
}
