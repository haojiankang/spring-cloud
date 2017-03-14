package com.ghit.framework.provider.sysmanager.api.model.vo.security;


import java.io.Serializable;

import com.ghit.framework.provider.sysmanager.api.supports.security.AuthenticationType;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IJurisdiction;

/**
 * 安全权限信息基础类
 * 
 * @author ren7wei
 *
 */
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

    @Override
    public AuthenticationType getAuthenticationType() {
        return type;
    }

    @Override
    public String getAuthenticationRule() {
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
