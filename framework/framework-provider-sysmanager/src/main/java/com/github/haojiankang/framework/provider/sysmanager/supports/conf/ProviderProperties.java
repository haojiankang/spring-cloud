/** 
 * Project Name:framework-consumer 
 * File Name:SecurityProperties.java 
 * Package Name:com.ghit.framework.consumer.conf 
 * Date:2017年3月14日下午5:25:23  
*/

package com.github.haojiankang.framework.provider.sysmanager.supports.conf;

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

@ConfigurationProperties(prefix = ProviderProperties.DS, ignoreUnknownFields = false)
public class ProviderProperties {
    protected static final String DS = "provider";
    public static class LoginPolicyProperties{
        private String[] types;
        public String[] getTypes() {
            return types;
        }
        public void setTypes(String[] types) {
            this.types = types;
        }
    }
    private LoginPolicyProperties login;
    public LoginPolicyProperties getLogin() {
        return login;
    }
    public void setLogin(LoginPolicyProperties login) {
        this.login = login;
    }
    

}
