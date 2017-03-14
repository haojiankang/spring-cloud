package com.ghit.framework.consumer.utils.security;

import java.util.Map;
import java.util.Set;

import com.ghit.framework.commons.utils.security.model.IUser;

/**
 * 会话管理接口
 * 
 * @author ren7wei
 *
 */
public interface SessionManager {
    /**
     * 根据session获取对应的作用域对象
     * 
     * @param session
     * @return
     */
    Map<String, Object> getScopeBySession(Session session);

    /**
     * 根据sessionid获取对应的作用域对象
     * 
     * @param session
     * @return
     */
    Map<String, Object> getScopeById(String sessionid);

    /**
     * 根据ip获取对应的作用域对象
     * 
     * @param session
     * @return
     */
    Map<String, Object> getScopeByIp(String ip);

    /**
     * 根据会话用户获取对应的作用域对象
     * 
     * @param session
     * @return
     */
    Map<String, Object> getScopeByUser(IUser user);

    /**
     * 想指定session的作用域存值
     * 
     * @param key
     * @param value
     */
    void put(Session session, String key, Object value);

    /**
     * 获得所有会话
     * 
     * @return
     */
    Set<Session> allSession();

    /**
     * 根据sessionid获取Session
     * 
     * @param sessionid
     * @return
     */
    Session getSessionById(String sessionid);

    /**
     * 获取当前会话
     * 
     * @return
     */
    Session getCurrentSession();

    /**
     * 设置当前会话
     * 
     * @return
     */
    void setCurrentSession(Session session);

    /**
     * 
     * getSessionByToken:根据session绑定的token获取session
     *
     * @author ren7wei
     * @param token
     * @return
     * @since JDK 1.8
     */
    Session getSessionByToken(String token);

    /**
     * 
     * startClear:启动一个监听定时清除无效会话.
     *
     * @author ren7wei
     * @param maxIdleTime
     * @since JDK 1.8
     */
    void startClear(long maxIdleTime);
    /**
     * 
     * generateId:给session生成id
     *
     * @author ren7wei
     * @param session 需要生成id的session
     * @since JDK 1.8
     */
    void generateId(Session session);
}
