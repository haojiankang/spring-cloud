package com.ghit.framework.provider.conf.initqueue;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = InitQueueProperties.DS, ignoreUnknownFields = false)
public class InitQueueProperties {

    // 对应配置文件里的配置键
    public final static String DS = "init.queue";
    private String contextName;
    private String[] async;
    private String[] sync;

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String[] getAsync() {
        return async;
    }

    public void setAsync(String[] async) {
        this.async = async;
    }

    public String[] getSync() {
        return sync;
    }

    public void setSync(String[] sync) {
        this.sync = sync;
    }
//
}