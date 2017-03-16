/** 
 * Project Name:ytj-manage-common 
 * File Name:SharpCommon.java 
 * Package Name:com.ghit.common 
 * Date:2017年2月16日下午4:30:45  
*/

package com.github.haojiankang.framework.consumer.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.haojiankang.framework.commons.utils.lang.StringUtil;
import com.github.haojiankang.framework.commons.utils.security.context.ContextContainer;
import com.github.haojiankang.framework.commons.utils.security.model.IUser;
import com.github.haojiankang.framework.consumer.ConsumerConstant;
import com.github.haojiankang.framework.consumer.conf.SecurityProperties;
import com.github.haojiankang.framework.consumer.utils.security.SecurityCoreManager;
import com.github.haojiankang.framework.consumer.utils.security.SecuritySession;
import com.github.haojiankang.framework.consumer.utils.security.Session;
import com.github.haojiankang.framework.consumer.utils.servlet.WebUtils;

/**
 * ClassName:SharpCommon <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年2月16日 下午4:30:45 <br/>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class CS {
    private static SecurityProperties conf;
    public static void setSecurityConfig(SecurityProperties config){
        CS.conf=config;
    }
    protected static final Log LOGGER = LogFactory.getLog(CS.class);
    private static CS sharp;

    public static CS sharp() {
        if (sharp == null)
            sharp = new CS();
        return sharp;
    }

    /**
     * 
     * error:存放错误消息. 同一个线程环境下可以跨越层传递消息
     * 
     * @author ren7wei
     * @param message
     * @return 工具类本身，用来实现链式调用
     * @since JDK 1.8
     */
    public static CS error(String message) {
        ContextContainer.getContainer().getContext().appendBind(ConsumerConstant.TCONTEXT_MESSAGE_ERROR, message);
        return sharp();
    }

    /**
     * 
     * error:获取错误消息.同一个线程环境下可以跨越层传递消息
     *
     * @author ren7wei
     * @return 调用之前存放的错误消息
     * @since JDK 1.8
     */
    public static String error() {
        return ContextContainer.getContainer().getContext().lookup(ConsumerConstant.TCONTEXT_MESSAGE_ERROR);
    }

    public static void clearError() {
        ContextContainer.getContainer().getContext().unbind(ConsumerConstant.TCONTEXT_MESSAGE_ERROR);
    }

    /**
     * 
     * message:获取提示消息.同一个线程环境下可以跨越层传递消息
     *
     * @author ren7wei
     * @return 调用之前存放的错误消息
     * @since JDK 1.8
     */
    public static String message() {
        return ContextContainer.getContainer().getContext().lookup(ConsumerConstant.TCONTEXT_MESSAGE_INFO);
    }

    /**
     * 
     * error:存放错误消息. 同一个线程环境下可以跨越层传递消息
     * 
     * @author ren7wei
     * @param message
     * @return 工具类本身，用来实现链式调用
     * @since JDK 1.8
     */
    public static CS message(String message) {
        ContextContainer.getContainer().getContext().appendBind(ConsumerConstant.TCONTEXT_MESSAGE_INFO, message);
        return sharp();
    }

    /**
     * 
     * lookup:在安全上下文中查找指定值.
     *
     * @author ren7wei
     * @param name
     * @return 查找到的值
     * @since JDK 1.8
     */
    public static <T extends Object> T lookup(String name) {
        return SecurityCoreManager.manager().getContext().lookup(name);
    }

    /**
     * 
     * currentUser:在MVC环境下获取当前用户信息，前提需要开会会话管理器.
     *
     * @author ren7wei
     * @return 当前登录的用户信息
     * @since JDK 1.8
     */
    public static IUser currentUser() {
        return SecurityCoreManager.manager().getSessionManager().getCurrentSession().currentUser();
    }

    public static Session currentSession() {
        return SecurityCoreManager.manager().getSessionManager().getCurrentSession();
    }

    public static Session session(String sessionId) {
        return SecurityCoreManager.manager().getSessionManager().getSessionById(sessionId);
    }

    /**
     * 
     * currentUser:给当前会话设置用户信息.
     *
     * @author ren7wei
     * @return 工具类，支持链式调用
     * @since JDK 1.8
     */
    public static CS currentUser(IUser user) {
        SecurityCoreManager.manager().getSessionManager().getCurrentSession().currentUser(user);
        return sharp();
    }

    /**
     * 
     * jsonToT:将JSON格式的字符串转换为指定的类型的实体
     *
     * @author ren7wei
     * @param c
     *            指定的类型
     * @param jsonStr
     *            JSON格式的字符串
     * @return
     * @since JDK 1.8
     */
    public static <T extends Object> T jsonToT(Class<T> c, String jsonStr) {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return ojbmapper.readValue(jsonStr, c);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    public static Object strToJsonObj(String jsonStr) {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return ojbmapper.readTree(jsonStr);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    public static String objToJson(Object obj) {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        try {
            return ojbmapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    /**
     * 
     * serialize:序列化对象为byte数组
     *
     * @author ren7wei
     * @param obj
     * @return
     * @since JDK 1.7
     */
    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            try (ObjectOutputStream os = new ObjectOutputStream(bos);) {
                os.writeObject(obj);
                os.flush();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            byte[] b = bos.toByteArray();
            return b;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 
     * deserialize:反序列化byte为对象.
     *
     * @author ren7wei
     * @param bs
     * @return
     * @since JDK 1.7
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bs) {
        try (ByteArrayInputStream ins = new ByteArrayInputStream(bs);) {
            try (ObjectInputStream in = new ObjectInputStream(ins);) {
                return (T) in.readObject();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 
     * backAjaxMessage:返回ajax
     *
     * @author ren7wei
     * @param state
     * @param message
     * @return
     * @since JDK 1.8
     */
    public static Map<String, String> backAjaxMessage(boolean state, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("state", String.valueOf(state));
        map.put("message", message);
        return map;
    }


    /**
     * 
     * md5:返回字符串的md5值.
     *
     * @author ren7wei
     * @param str
     * @return
     * @since JDK 1.8
     */
    public static String md5(String str) {
        // 实际返回的为MD5值
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 对输入流进行散列运算, 支持md5与sha1算法.
     * 
     * @param input
     *            待运算输入流
     * @param algorithm
     *            MD2/MD5/SHA-1/SHA-256/SHA-384/SHA-512
     * @return 散列运算得到的字节数组
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] digest(InputStream input, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        int read = -1;
        int bufferLength = 1024 * 1024 * 10;
        byte[] buffer = new byte[bufferLength];
        do {
            read = input.read(buffer, 0, bufferLength);
            if (read > -1)
                messageDigest.update(buffer, 0, read);
        } while (read > -1);
        return messageDigest.digest();
    }

    public static final String JSESSIONID = "hsessionid";
    public static final String TOKEN = "htoken";

    /**
     * 
     * getJsessionId:获取jsessionid.
     *
     * @author ren7wei
     * @param request
     * @return
     * @since JDK 1.8
     */
    public static String getJsessionId(ServletRequest request) {
        String jsessionid = null;
        if (conf.getEnableParam()) {
            jsessionid = request.getParameter(JSESSIONID);
        }
        if (StringUtil.isEmpty(jsessionid)) {
            Cookie cookie = WebUtils.getCookie((HttpServletRequest) request, JSESSIONID);
            if (cookie != null)
                jsessionid = cookie.getValue();
        }
        return jsessionid;
    }

    /**
     * 
     * getToken:获取token
     *
     * @author ren7wei
     * @param request
     * @return
     * @since JDK 1.8
     */
    public static String getToken(ServletRequest request) {
        String token = request.getParameter(TOKEN);
        if (StringUtil.isEmpty(token)) {
            Cookie cookie = WebUtils.getCookie((HttpServletRequest) request, TOKEN);
            if (cookie != null)
                token = cookie.getValue();
        }
        return token;
    }

    /**
     * 
     * getCurrentSession:获取当前会话
     *
     * @author ren7wei
     * @param request
     * @param response
     * @return
     * @since JDK 1.8
     */
    public static Session getCurrentSession(HttpServletRequest request, HttpServletResponse response) {
        String url=request.getRequestURI().replace(request.getContextPath(), "");;
        String login = conf.getLoginUrl();
        login = "(" + login.replace("*", ".*").replace(",", ")|(") + ")";
        
        String jsessionId = CS.getJsessionId(request);
        String token = getToken(request);
        // 设置当前会话
        SecurityCoreManager manager = SecurityCoreManager.manager();
        Session currentSession = null;
        // 优先使用token查找sessoin
        if (StringUtils.isNotEmpty(token)) {
            currentSession = manager.getSessionManager().getSessionByToken(token);
        } else if (StringUtils.isNotEmpty(jsessionId)) {
            currentSession = manager.getSessionManager().getSessionById(jsessionId);
        }
        // 无会话则初始化会话
        if (currentSession == null) {
            currentSession = new SecuritySession();
            currentSession.sessionIp(WebUtils.getIpAddress(request));
            if (!StringUtil.isEmpty(jsessionId)) {
                currentSession.sessionId(jsessionId);
            } else {
                manager.getSessionManager().generateId(currentSession);
            }
            manager.getSessionManager().put(currentSession, "lastAccessTime", System.currentTimeMillis());
        }
        // 访问登录链接更新session的ip地址，并同时清空当前会话用户
        if (url.matches(login)) {
            currentSession.sessionIp(WebUtils.getIpAddress(request));
            currentSession.currentUser(null);
        }
        manager.getSessionManager().setCurrentSession(currentSession);
        try {
            setCookie(request, response, currentSession);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        request.setAttribute(CS.JSESSIONID, currentSession.sessionId());
        return currentSession;
    }

    /**
     * 
     * setCookie:将session信息写入cookie
     *
     * @author ren7wei
     * @param response
     *            http响应对象
     * @param session
     *            会话信息
     * @throws UnsupportedEncodingException
     * @since JDK 1.8
     */
    public  static void setCookie(HttpServletRequest request, HttpServletResponse response, Session session)
            throws UnsupportedEncodingException {

        String contextPath = request.getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        if (session.sessionId() != null) {
            if (StringUtil.isEmpty(WebUtils.getCookieValue(request, CS.JSESSIONID))) {
                WebUtils.addCookie(response, CS.JSESSIONID, session.sessionId(), contextPath, -1, false, false);
            }
        }
//        if (session.token() != null) {
//            if (StringUtil.isEmpty(WebUtils.getCookieValue(request, TOKEN))) {
//                WebUtils.addCookie(response, TOKEN, session.token(), contextPath, -1, false, false);
//            }
//        }
    }

}
