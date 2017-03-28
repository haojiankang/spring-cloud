package com.haojiankang.framework.consumer.utils.security;

import com.haojiankang.framework.commons.utils.security.context.Context;
import com.haojiankang.framework.commons.utils.security.context.HashTableContext;
import com.haojiankang.framework.commons.utils.security.model.IUser;

/**
 * 安全管理器
 * 
 * @author ren7wei
 *
 */
public class SecurityCoreManager {
    private static SecurityCoreManager securityManager = null;
    private Context context;
    private SessionManager sessionManager;

    private SecurityCoreManager() {
        // 默认采用Table实现的上下文
        context = new HashTableContext();
        sessionManager = new SessionManagerMap();
    }

    /**
     * 返回安全管理器实例
     * 
     * @return
     */
    public static SecurityCoreManager manager() {
        if (securityManager == null)
            securityManager = new SecurityCoreManager();
        return securityManager;
    }

    /**
     * 认证用户是否具有指定路径的访问权限
     * 
     * @param url
     * @param user
     * @return
     */
    public boolean urlAuth(String url, IUser user) {
        return url.matches(
                "(" + user.authenticationURL().replace(".", "\\.").replace("*", ".*").replace(",", ")|(") + ")");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

}
