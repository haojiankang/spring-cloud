package com.ghit.framework.provider.sysmanager.api.supports.security.model;

import java.io.Serializable;

import com.ghit.framework.provider.sysmanager.api.supports.security.AuthenticationType;

/**
 * 权限信息接口
 * 
 * @author ren7wei
 *
 */
public interface IJurisdiction extends Serializable{
    /**
     * 获取权限编码
     * 
     * @return
     */
    String getCode();

    /**
     * 获取认证类型
     * 
     * @return
     */
    AuthenticationType getAuthenticationType();

    /**
     * 获取认证规则
     * 
     * @return
     */
    String getAuthenticationRule();

}
