package com.quoteflow.controller;

import com.quoteflow.dto.ApiResponse;
import com.quoteflow.dto.ProjectCreateRequest;
import com.quoteflow.dto.ProjectSubmitVO;
import com.quoteflow.service.ProjectQuoteService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目立项与报价 REST 控制器。
 *
 * @author QuoteFlow
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectQuoteController {

    /**
     * 项目立项与报价服务。
     */
    private final ProjectQuoteService projectQuoteService;

    public ProjectQuoteController(ProjectQuoteService projectQuoteService) {
        this.projectQuoteService = projectQuoteService;
    }

    /**
     * 提交项目立项与报价审批。
     *
     * @param request 项目创建请求
     * @return 项目审批提交响应
     */
    @PostMapping
    public ApiResponse<ProjectSubmitVO> submitProjectApproval(@Valid @RequestBody ProjectCreateRequest request) {
        return ApiResponse.success(projectQuoteService.submitProjectApproval(request));
    }
}
