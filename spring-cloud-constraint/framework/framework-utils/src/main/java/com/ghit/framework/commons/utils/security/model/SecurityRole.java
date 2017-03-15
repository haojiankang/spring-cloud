package com.ghit.framework.commons.utils.security.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private List<SecurityJurisdiction> jurisdictions=new ArrayList<>();

    @Override
    public List<SecurityJurisdiction> getJurisdiction() {
        return jurisdictions;
    }

    public void setJurisdictions(List<SecurityJurisdiction> jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

}
