package com.haojiankang.framework.consumer.utils.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.haojiankang.framework.commons.utils.i18n.I118nUtils;
import com.haojiankang.framework.consumer.conf.SecurityProperties;
import com.haojiankang.framework.consumer.utils.security.SecurityCoreManager;
import com.haojiankang.framework.consumer.utils.security.Session;
import com.haojiankang.framework.consumer.utils.servlet.WebUtils;

/**
 * 会话管理核心过滤器 此过滤器应该放在所有需要会话的过滤器之前
 * 
 */
public class SecurityManagerFilter implements Filter {
    protected final Log logger = LogFactory.getLog(getClass());
    private SecurityProperties conf;
    public SecurityManagerFilter(SecurityProperties conf) {
        this.conf=conf;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        l: {
            String notSecurity = conf.getNotSecurity();
            notSecurity = "(" + notSecurity.replace("*", ".*").replace(",", ")|(") + ")";
            // 不需要会话处理的路径,直接转交给下一个filter
            if (notSecurity != null
                    && httpRequest.getRequestURI().replace(httpRequest.getContextPath(), "").matches(notSecurity)) {
                break l;
            }
            // 设置当前会话
            SecurityCoreManager manager = SecurityCoreManager.manager();
            Session currentSession = manager.getSessionManager().getCurrentSession();
            // 登录检查
            if (testIsLogin(currentSession, httpRequest, httpRequest, response)) {
                return;
            }
            // 权限检查
            if (urlAuth(request, response, httpRequest, manager, currentSession)) {
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 
     * urlAuth:权限认证.
     *
     * @author ren7wei
     * @param request
     * @param response
     * @param httpRequest
     * @param manager
     * @param currentSession
     * @throws ServletException
     * @throws IOException
     * @since JDK 1.8
     */
    private boolean urlAuth(ServletRequest request, ServletResponse response, HttpServletRequest httpRequest,
            SecurityCoreManager manager, Session currentSession) throws ServletException, IOException {
        // 当前会话无用户，或者用户权限不足则重新定位页面
        if (!manager.urlAuth(httpRequest.getRequestURI().replace(httpRequest.getContextPath(), ""),
                currentSession.currentUser())) {
            if (WebUtils.isAjax(httpRequest)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("state", false);
                map.put("message", I118nUtils.get("security.web.access.auth"));
                WebUtils.reponseJons((HttpServletResponse) response, map);
            } else {
                request.setAttribute("message", I118nUtils.get("security.web.access.auth"));
                request.getRequestDispatcher(conf.getErrorPage()).forward(request, response);
            }
            return true;
        }
        return false;
    }

    /**
     * 
     * testIsLogin:检查用户是否登录，未登录要求先进行登录.
     *
     * @author ren7wei
     * @param currentSession
     * @param httpRequest
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @since JDK 1.8
     */
    private boolean testIsLogin(Session currentSession, HttpServletRequest httpRequest, ServletRequest request,
            ServletResponse response) throws ServletException, IOException {
        if (currentSession == null || currentSession.currentUser() == null) {
            if (WebUtils.isAjax(httpRequest)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("state", false);
                map.put("message", I118nUtils.get("security.web.access.nologin"));
                WebUtils.reponseJons((HttpServletResponse) response, map);
            } else {
                request.setAttribute("message", I118nUtils.get("security.web.access.nologin"));
                request.getRequestDispatcher(conf.getErrorPage()).forward(request, response);
            }
            return true;
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

}
