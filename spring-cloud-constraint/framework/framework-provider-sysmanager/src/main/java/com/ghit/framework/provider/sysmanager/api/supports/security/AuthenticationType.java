package com.ghit.framework.provider.sysmanager.api.supports.security;

/**
 * 认证类型枚举
 * 
 * @author ren7wei
 *
 */
public enum AuthenticationType {
    /**
     * 路径认证
     */
    URL;
    public static AuthenticationType parser(String stype) {
        int type = Integer.parseInt(stype);
        switch (type) {
        case 1:
            return URL;
        }
        return null;
    }
}
