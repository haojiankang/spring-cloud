package com.github.haojiankang.framework.provider.sysmanager.api.model.vo.sysmgr;

import com.github.haojiankang.framework.provider.sysmanager.api.supports.vo.BaseVO;

public class VOJurisdiction  extends BaseVO {

    private static final long serialVersionUID = 1551593330856857009L;
    private String authenticationRule;
    private String authenticationType;
    private String jurisdictionName;
    private String jurisdictionCode;

    public String getAuthenticationRule() {
        return authenticationRule;
    }

    public void setAuthenticationRule(String authenticationRule) {
        this.authenticationRule = authenticationRule;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getJurisdictionName() {
        return jurisdictionName;
    }

    public void setJurisdictionName(String jurisdictionName) {
        this.jurisdictionName = jurisdictionName;
    }

    public String getJurisdictionCode() {
        return jurisdictionCode;
    }

    public void setJurisdictionCode(String jurisdictionCode) {
        this.jurisdictionCode = jurisdictionCode;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
