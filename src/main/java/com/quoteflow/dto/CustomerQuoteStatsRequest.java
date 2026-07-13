package com.quoteflow.dto;

import javax.validation.constraints.NotBlank;

/**
 * 客户报价统计请求。
 *
 * @author zhangyujie
 */
public class CustomerQuoteStatsRequest {

    /**
     * 选中的项目表单数据 ID，多个用英文逗号分隔。
     */
    @NotBlank
    private String formUserIds;

    public String getFormUserIds() {
        return formUserIds;
    }

    public void setFormUserIds(String formUserIds) {
        this.formUserIds = formUserIds;
    }
}
