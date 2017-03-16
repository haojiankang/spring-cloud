package com.github.haojiankang.framework.consumer.utils.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.haojiankang.framework.commons.utils.security.model.IUser;

public class SecuritySession implements Session {
    private String id;
    private String token;
    private String ip;
    private IUser user;
    private Date accessTime;
    private Map<String, Object> message;
    {
        accessTime = new Date();
        message = new HashMap<>();
    }

    @Override
    public String sessionId() {
        return id;
    }

    @Override
    public String sessionIp() {
        return ip;
    }

    @Override
    public IUser currentUser() {
        return user;
    }

    @Override
    public void sessionId(String id) {
        this.id = id;
    }

    @Override
    public void sessionIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void currentUser(IUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Session))
            return false;
        return StringUtils.equals(this.id,(((Session) obj).sessionId()));
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public Date lastAccessTime() {
        return accessTime;
    }

    @Override
    public void setAccessTime(Date now) {
        accessTime = now;
    }

    @Override
    public void setMessage(String key, Object value) {
        synchronized (this) {
            message.put(key, value);
        }
    }

    @Override
    public Object getMessage(String key) {
        synchronized (this) {
            return message.get(key);
        }
    }

    @Override
    public void delMessage(String key) {
        synchronized (this) {
            message.remove(key);
        }
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public void token(String token) {
        this.token = token;
    }
}
