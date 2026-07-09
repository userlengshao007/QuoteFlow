package com.quoteflow.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Contact transfer object.
 *
 * @author QuoteFlow
 */
public class ContactDTO {

    /**
     * Chaoxing user uid or passport id.
     */
    @NotNull
    private Long uid;

    /**
     * User display name.
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
