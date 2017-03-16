package com.github.haojiankang.framework.consumer.utils.security.filter;

import java.io.IOException;
import java.util.Date;
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

import com.github.haojiankang.framework.commons.utils.i18n.I118nUtils;
import com.github.haojiankang.framework.commons.utils.lang.StringUtil;
import com.github.haojiankang.framework.commons.utils.security.context.ContextContainer;
import com.github.haojiankang.framework.consumer.conf.SecurityProperties;
import com.github.haojiankang.framework.consumer.utils.CS;
import com.github.haojiankang.framework.consumer.utils.security.SecurityCoreManager;
import com.github.haojiankang.framework.consumer.utils.security.Session;
import com.github.haojiankang.framework.consumer.utils.servlet.WebUtils;

/**
 * 会话管理核心过滤器 此过滤器应该放在所有需要会话的过滤器之前
 * 
 */
public class SessionManagerFilter implements Filter {
    protected final Log logger = LogFactory.getLog(getClass());
    private SecurityProperties conf;
    public SessionManagerFilter(SecurityProperties conf) {
        this.conf=conf;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String url = httpRequest.getRequestURI().replace(httpRequest.getContextPath(), "");
            String exclude = conf.getExclude();
            exclude = "(" + exclude.replace("*", ".*").replace(",", ")|(") + ")";
            // 不需要会话处理的路径,直接转交给下一个filter
            if (url.matches(exclude)) {
                chain.doFilter(request, response);
                return;
            }
            String login = conf.getLoginUrl();
            login = "(" + login.replace("*", ".*").replace(",", ")|(") + ")";
            int timeoutMinute = conf.getTimeout();
            Session currentSession = CS.getCurrentSession(httpRequest, (HttpServletResponse) response);
            // 客户端信息发生变化后不再继续向下执行
            // if (clientInfoChange(response, httpRequest, url, currentSession,
            // login)) {
            // return;
            // }
            // 会话超时不再继续向下执行
            if (sessionTimeOut(response, httpRequest, url, currentSession, login, timeoutMinute)) {
                return;
            }
            chain.doFilter(request, response);
        } finally {
            // 清除Context
            ContextContainer.getContainer().destroyContext();
        }
    }

    /**
     * 
     * clientInfoChange:判断客户端信息是否发生改变
     * 
     * @author ren7wei
     * @param response
     * @param httpRequest
     * @param url
     * @param currentSession
     * @return
     * @throws ServletException
     * @throws IOException
     * @since JDK 1.8
     */
    @SuppressWarnings("unused")
    @Deprecated
    private boolean clientInfoChange(ServletResponse response, HttpServletRequest httpRequest, String url,
            Session currentSession, String login) throws ServletException, IOException {
        // 访问非登录url时，如果上下文中保存的会话的访问IP和当前访问Ip地址不同，并且auth_fail_url不为空，则重定位页面到auth_fail_url
        if (!url.matches(login) && !StringUtil.eq(currentSession.sessionIp(), WebUtils.getIpAddress(httpRequest))) {
            SecurityCoreManager.manager().getSessionManager().setCurrentSession(null);
            reponseErrMessage(httpRequest, response, I118nUtils.get("security.web.ip.error"));
            return true;
        }
        return false;
    }

    private boolean sessionTimeOut(ServletResponse response, HttpServletRequest httpRequest, String url,
            Session currentSession, String login, int timeoutMinute) throws ServletException, IOException {
        // 访问非登陆url时，会话超时处理
        if (!url.matches(login)
                && System.currentTimeMillis() - currentSession.lastAccessTime().getTime() > 1000 * 60 * timeoutMinute) {
            reponseErrMessage(httpRequest, response, I118nUtils.get("security.web.session.timeout"));
            return true;
        } else {
            // 更新sessionid，ip，accesstime
            currentSession.setAccessTime(new Date());
            return false;
        }
    }

    /**
     * 
     * reponseErr:响应错误页面
     * 
     * @author ren7wei
     * @param request
     * @param response
     * @param httpRequest
     * @throws ServletException
     * @throws IOException
     * @since JDK 1.7
     */
    private void reponseErrMessage(HttpServletRequest request, ServletResponse response, String message)
            throws ServletException, IOException {
        if (WebUtils.isAjax(request)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("state", false);
            map.put("message", message);
            WebUtils.reponseJons((HttpServletResponse) response, map);
        } else {
            request.setAttribute("message", message);
            request.getRequestDispatcher(conf.getErrorPage()).forward(request, response);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

}
