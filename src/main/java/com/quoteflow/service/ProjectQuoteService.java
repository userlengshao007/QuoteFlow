package com.quoteflow.service;

import com.quoteflow.dto.ProjectCreateRequest;
import com.quoteflow.dto.ProjectSubmitVO;

/**
 * Project quote service.
 *
 * @author QuoteFlow
 */
public interface ProjectQuoteService {

    /**
     * Submits project quote approval.
     *
     * @param request project creation request
     * @return project approval submission result
     */
    ProjectSubmitVO submitProjectApproval(ProjectCreateRequest request);
}
