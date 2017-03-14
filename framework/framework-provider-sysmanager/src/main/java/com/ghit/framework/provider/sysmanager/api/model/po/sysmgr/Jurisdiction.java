package com.ghit.framework.provider.sysmanager.api.model.po.sysmgr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ghit.framework.provider.sysmanager.api.supports.DbOnly;
import com.ghit.framework.provider.sysmanager.api.supports.po.BaseEntity;

@Entity
@Table(name = "HJK_SYS_JURISDICTION")
public class Jurisdiction extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1551593330856857009L;
    @Column(name = "authentication_rule", length = 100)
    private String authenticationRule;
    @Column(name = "authentication_type", length = 30)
    private String authenticationType;
    @Column(name = "jurisdiction_name", length = 60)
    private String jurisdictionName;
    @DbOnly(name = "权限编码")
    @Column(name = "jurisdiction_code", length = 60)
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
