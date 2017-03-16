package com.ghit.framework.provider.sysmanager.api.model.vo.sysmgr;

import java.util.ArrayList;
import java.util.List;

import com.ghit.framework.provider.sysmanager.api.supports.vo.BaseVO;

public class VORole  extends BaseVO{
    {
        jurisdictions=new ArrayList<>();
    }
    private static final long serialVersionUID = -3022168779714627416L;
    private String roleName;

    private String description;

    private String roleCode;
    private List<VOJurisdiction> jurisdictions;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<VOJurisdiction> getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(List<VOJurisdiction> jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

}
