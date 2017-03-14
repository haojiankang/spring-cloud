package com.ghit.framework.provider.sysmanager.api.model.vo.security;


import java.io.Serializable;
import java.util.List;

import com.ghit.framework.provider.sysmanager.api.supports.security.model.IJurisdiction;
import com.ghit.framework.provider.sysmanager.api.supports.security.model.IRole;

/**
 * 安全角色信息基础类
 * 
 * @author ren7wei
 *
 */
public class SecurityRole implements IRole, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<? extends IJurisdiction> jurisdictions;

    @Override
    public List<? extends IJurisdiction> getJurisdiction() {
        return jurisdictions;
    }

    public void setJurisdictions(List<? extends IJurisdiction> jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

}
