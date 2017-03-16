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

import com.github.haojiankang.framework.commons.utils.RequestHeaders;
import com.github.haojiankang.framework.commons.utils.bean.BeanUtils;
import com.github.haojiankang.framework.commons.utils.security.context.ContextContainer;
import com.github.haojiankang.framework.commons.utils.security.model.IUser;
import com.github.haojiankang.framework.commons.utils.security.model.SecurityUser;
import com.github.haojiankang.framework.provider.utils.PS;

/**
 * 1.用户拦截请求，在请求头中包含userinfo字段时，将userinfo转换为IUser实体。并存放到PS.currentUser中。<br/>
 * 2.清除context上下文中的信息。
 * 
 */
public class UserHeaderFilter implements Filter {
    protected final Log logger = LogFactory.getLog(getClass());

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {
            String userInfo = httpRequest.getHeader(RequestHeaders.USER_INFO);
            if (userInfo != null) {
                IUser user = BeanUtils.decodeHeader(userInfo, SecurityUser.class);
                PS.currentUser(user);
            }
            chain.doFilter(request, response);
        } finally {
            PS.currentUser(null);
            ContextContainer.getContainer().destroyContext();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
