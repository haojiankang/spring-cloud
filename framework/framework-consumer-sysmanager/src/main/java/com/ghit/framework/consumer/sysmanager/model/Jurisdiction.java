/** 
 * Project Name:framework-consumer-sysmanager 
 * File Name:Jurisdiction.java 
 * Package Name:com.ghit.framework.consumer.sysmanager.model 
 * Date:2017年3月15日下午1:50:39  
*/

package com.ghit.framework.consumer.sysmanager.model;

import com.ghit.framework.consumer.utils.model.BaseModel;

/**
 * ClassName:Jurisdiction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月15日 下午1:50:39 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class Jurisdiction extends BaseModel{
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
