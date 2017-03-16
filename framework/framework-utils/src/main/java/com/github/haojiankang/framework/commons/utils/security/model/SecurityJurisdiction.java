package com.github.haojiankang.framework.commons.utils.security.model;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.haojiankang.framework.commons.utils.security.AuthenticationType;

/**
 * 安全权限信息基础类
 * 
 * @author ren7wei
 *
 */

@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class SecurityJurisdiction implements IJurisdiction, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private AuthenticationType type;
    private String rule;

    @Override
    public String getCode() {
        return code;
    }

    public AuthenticationType getType() {
        return type;
    }

    public String getRule() {
        return rule;
    }
    public void setType(AuthenticationType type) {
        this.type = type;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
