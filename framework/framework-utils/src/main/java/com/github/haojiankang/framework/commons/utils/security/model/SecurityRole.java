package com.github.haojiankang.framework.commons.utils.security.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 安全角色信息基础类
 * 
 * @author ren7wei
 *
 */

@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
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
