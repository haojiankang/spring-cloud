package com.ghit.framework.consumer.utils.security;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ghit.framework.commons.utils.lang.StringUtil;
import com.ghit.framework.commons.utils.security.model.IUser;
import com.ghit.framework.consumer.ConsumerConstant;

/**
 * Map方式实现的会话管理器
 * 
 * @author ren7wei
 *
 */
public class SessionManagerMap implements SessionManager {
    protected final Log logger = LogFactory.getLog(getClass());
    // 声明线程容器用以存放当前session
    ThreadLocal<Session> currentSession;
    {
        currentSession = new ThreadLocal<Session>();
    }

    @Override
    public void startClear(long maxIdleTime) {
        // 定时清理空闲时间过长的会话，3个小时清除一次，最长空闲时间由输入参数决定
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<Session, Map<String, Object>> all = getSessionContext();
                Iterator<Entry<Session, Map<String, Object>>> iterator = all.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<Session, Map<String, Object>> next = iterator.next();
                    if ((System.currentTimeMillis() - next.getKey().lastAccessTime().getTime()) > maxIdleTime * 60
                            * 1000) {
                        iterator.remove();
                    }
                }
            }
        }, 0, 3 * 60 * 60 * 1000);
    }

    /**
     * 从securityContext中获取sessionContext
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    Map<Session, Map<String, Object>> getSessionContext() {
        // 默认将会话管理器存放在securityContext中。key值为Constant.CONTEXT_SESSION_CONTEXT
        SecurityCoreManager manager = SecurityCoreManager.manager();
        Map<Session, Map<String, Object>> sessionContext = null;
        Object context = manager.getContext().lookup(ConsumerConstant.CONTEXT_SESSION_CONTEXT);
        if (context == null) {
            sessionContext = new Hashtable<Session, Map<String, Object>>();
            manager.getContext().bind(ConsumerConstant.CONTEXT_SESSION_CONTEXT, sessionContext);
            context = manager.getContext().lookup(ConsumerConstant.CONTEXT_SESSION_CONTEXT);
        }
        sessionContext = (Map<Session, Map<String, Object>>) context;
        return sessionContext;
    }

    @Override
    public Map<String, Object> getScopeBySession(Session session) {
        return getSessionContext().get(session);
    }

    @Override
    public Map<String, Object> getScopeById(String sessionid) {
        Iterator<Session> iterator = getSessionContext().keySet().iterator();
        while (iterator.hasNext()) {
            Session next = iterator.next();
            if (StringUtil.eq(next.sessionId(), sessionid))
                return getSessionContext().get(next);
        }
        return null;
    }

    @Override
    public Map<String, Object> getScopeByIp(String ip) {
        Iterator<Session> iterator = getSessionContext().keySet().iterator();
        while (iterator.hasNext()) {
            Session next = iterator.next();
            if (StringUtil.eq(next.sessionIp(), ip))
                return getSessionContext().get(next);
        }
        return null;
    }

    @Override
    public Map<String, Object> getScopeByUser(IUser user) {
        Iterator<Session> iterator = getSessionContext().keySet().iterator();
        while (iterator.hasNext()) {
            Session next = iterator.next();
            if (next.currentUser() != null && user.getUserName().equals(next.currentUser().getUserName()))
                return getSessionContext().get(next);
        }
        return null;
    }

    @Override
    public void put(Session session, String key, Object value) {
        Map<String, Object> context = getScopeBySession(session);
        if (context == null) {
            context = new Hashtable<String, Object>();
            getSessionContext().put(session, context);
        }
        context.put(key, value);
    }

    @Override
    public Set<Session> allSession() {
        return getSessionContext().keySet();
    }

    @Override
    public Session getSessionById(String sessionid) {
        Iterator<Session> iterator = allSession().iterator();
        while (iterator.hasNext()) {
            Session next = iterator.next();
            if (StringUtil.eq(next.sessionId(), sessionid))
                return next;
        }
        return null;
    }

    @Override
    public Session getSessionByToken(String token) {
        Iterator<Session> iterator = allSession().iterator();
        while (iterator.hasNext()) {
            Session next = iterator.next();
            if (StringUtil.eq(next.token(), token))
                return next;
        }
        return null;
    }

    @Override
    public Session getCurrentSession() {
        return currentSession.get();
    }

    @Override
    public void setCurrentSession(Session session) {
        currentSession.set(session);
    }

    @Override
    public void generateId(Session session) {
        UUID uuid = UUID.randomUUID();
        session.sessionId(uuid.toString());
    }
}
