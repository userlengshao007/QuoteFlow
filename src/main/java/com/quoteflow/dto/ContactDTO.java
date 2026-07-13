package com.quoteflow.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 联系人传输对象。
 *
 * @author zhangyujie
 */
public class ContactDTO {

    /**
     * 超星用户 UID 或 passportId。
     */
    @NotNull
    private Long uid;

    /**
     * 用户展示名称。
     */
    @NotBlank
    private String name;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
