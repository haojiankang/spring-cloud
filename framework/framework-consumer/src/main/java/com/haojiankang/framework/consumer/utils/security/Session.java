package com.haojiankang.framework.consumer.utils.security;

import java.util.Date;

import com.haojiankang.framework.commons.utils.security.model.IUser;

/**
 * 会话信息接口
 * 
 * @author ren7wei
 *
 */
public interface Session {
    /**
     * 返回会话ID
     * 
     * @return
     */
    String sessionId();

    /**
     * 设置sessionId
     * 
     * @param id
     */
    void sessionId(String id);

    /**
     * 返回会话所属IP地址
     */
    String sessionIp();

    /**
     * 设置sessionIp
     * 
     * @param ip
     */
    void sessionIp(String ip);

    /**
     * 会话当前用户
     * 
     * @return
     */
    IUser currentUser();

    /**
     * 设置当前用户
     * 
     * @param user
     */
    void currentUser(IUser user);

    /**
     * 最后一次访问时间
     * 
     * @return
     */
    Date lastAccessTime();

    /**
     * 设置当前访问时间
     * 
     * @param now
     */
    void setAccessTime(Date now);

    /**
     * 
     * setMessage:存储会话消息
     * 
     * @author ren7wei
     * @param key
     * @since JDK 1.8
     */
    void setMessage(String key, Object value);

    /**
     * 
     * getMessage:获取会话消息
     *
     * @author ren7wei
     * @param key
     * @return
     * @since JDK 1.8
     */
    Object getMessage(String key);

    /**
     * 
     * delMessage:删除会话消息
     * 
     * @author ren7wei
     * @param key
     * @since JDK 1.8
     */
    void delMessage(String key);
    /**
     * 
     * token:获取session的token值
     * @author ren7wei
     * @return
     * @since JDK 1.8
     */
    String token();
    /**
     * 
     * token:设置session的token值.
     *
     * @author ren7wei
     * @param token
     * @since JDK 1.8
     */
    void token(String token);
    
}
