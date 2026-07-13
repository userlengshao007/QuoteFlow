package com.quoteflow.service;

import com.quoteflow.dto.ProjectCreateRequest;
import com.quoteflow.dto.ProjectSubmitVO;

/**
 * 项目立项与报价服务。
 *
 * @author zhangyujie
 */
public interface ProjectQuoteService {

    /**
     * 提交项目立项与报价审批。
     *
     * @param request 项目创建请求
     * @return 项目审批提交结果
     */
    ProjectSubmitVO submitProjectApproval(ProjectCreateRequest request);
}
