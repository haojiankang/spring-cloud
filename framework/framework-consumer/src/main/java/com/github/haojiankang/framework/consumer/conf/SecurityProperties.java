/** 
 * Project Name:framework-consumer 
 * File Name:SecurityProperties.java 
 * Package Name:com.ghit.framework.consumer.conf 
 * Date:2017年3月14日下午5:25:23  
*/

package com.github.haojiankang.framework.consumer.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName:SecurityProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年3月14日 下午5:25:23 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */

@ConfigurationProperties(prefix = SecurityProperties.DS, ignoreUnknownFields = false)
public class SecurityProperties {
    protected static final String DS = "session";
    private Integer timeout;
    private String errorPage;
    private String exclude;
    private String notSecurity;
    private String loginUrl;
    private Boolean enableParam;
    {
        timeout = 30;
        enableParam = false;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getNotSecurity() {
        return notSecurity;
    }

    public void setNotSecurity(String notSecurity) {
        this.notSecurity = notSecurity;
    }

    public Boolean getEnableParam() {
        return enableParam;
    }

    public void setEnableParam(Boolean enableParam) {
        this.enableParam = enableParam;
    }

}
