package com.github.haojiankang.framework.provider.utils.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.github.haojiankang.framework.commons.utils.security.model.IUser;
import com.github.haojiankang.framework.commons.utils.security.model.SecurityUser;
import com.github.haojiankang.framework.provider.utils.PS;

/**
 * 会话管理核心过滤器 此过滤器应该放在所有需要会话的过滤器之前
 * 
 */
public class UserHeaderFilter implements Filter {
    protected final Log logger = LogFactory.getLog(getClass());

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userInfo = httpRequest.getHeader("userinfo");
        if (userInfo != null) {
            IUser user = BeanUtils.decodeHeader(userInfo,SecurityUser.class);
            PS.currentUser(user);
        }
        chain.doFilter(request, response);
        PS.currentUser(null);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
