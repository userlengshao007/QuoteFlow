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
 * Project quote REST controller.
 *
 * @author QuoteFlow
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectQuoteController {

    /**
     * Project quote service.
     */
    private final ProjectQuoteService projectQuoteService;

    public ProjectQuoteController(ProjectQuoteService projectQuoteService) {
        this.projectQuoteService = projectQuoteService;
    }

    /**
     * Submits project quote approval.
     *
     * @param request project creation request
     * @return project approval submission response
     */
    @PostMapping
    public ApiResponse<ProjectSubmitVO> submitProjectApproval(@Valid @RequestBody ProjectCreateRequest request) {
        return ApiResponse.success(projectQuoteService.submitProjectApproval(request));
    }
}
