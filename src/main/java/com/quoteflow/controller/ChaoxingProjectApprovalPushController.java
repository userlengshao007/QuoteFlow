package com.quoteflow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quoteflow.dto.ApiResponse;
import com.quoteflow.dto.ProjectApprovalPushResultVO;
import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import com.quoteflow.service.SalesPerformanceService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 超星项目审批数据推送控制器。
 *
 * @author zhangyujie
 */
@RestController
@RequestMapping("/api/chaoxing/project-approval")
public class ChaoxingProjectApprovalPushController {

    /**
     * 日志记录器。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChaoxingProjectApprovalPushController.class);

    /**
     * 销售业绩服务。
     */
    private final SalesPerformanceService salesPerformanceService;

    /**
     * JSON 转换器。
     */
    private final ObjectMapper objectMapper;

    public ChaoxingProjectApprovalPushController(SalesPerformanceService salesPerformanceService,
                                                 ObjectMapper objectMapper) {
        this.salesPerformanceService = salesPerformanceService;
        this.objectMapper = objectMapper;
    }

    /**
     * 接收项目立项审批数据推送。
     *
     * @param rawBody 原始请求体
     * @param requestParamMap URL 或表单参数
     * @return 处理结果
     */
    @PostMapping("/push")
    public ApiResponse<ProjectApprovalPushResultVO> handleProjectApprovalPush(
            @RequestBody(required = false) String rawBody,
            @RequestParam Map<String, String> requestParamMap) {
        Map<String, Object> payload = parsePayload(rawBody, requestParamMap);
        LOGGER.info("收到项目立项审批数据推送，payload={}", toJson(payload));
        return ApiResponse.success(salesPerformanceService.createPerformanceByApprovalPush(payload));
    }

    private Map<String, Object> parsePayload(String rawBody, Map<String, String> requestParamMap) {
        if (StringUtils.hasText(rawBody) && rawBody.trim().startsWith("{")) {
            try {
                return objectMapper.readValue(rawBody, new TypeReference<Map<String, Object>>() {
                });
            } catch (JsonProcessingException exception) {
                throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "数据推送请求体不是合法 JSON", exception);
            }
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
            payload.put(entry.getKey(), entry.getValue());
        }
        return payload;
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException exception) {
            LOGGER.warn("项目立项审批推送 payload 序列化失败", exception);
            return "{}";
        }
    }
}
