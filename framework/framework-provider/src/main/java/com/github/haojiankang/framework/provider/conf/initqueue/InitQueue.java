package com.github.haojiankang.framework.provider.conf.initqueue;
/** 
 * Project Name:EHealthData 
 * File Name:InitQueue.java 
 * Package Name:com.ghit.common 
 * Date:2016年8月8日上午11:03:22  
*/

import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * ClassName:InitQueue <br>
 * Function: 初始化队列，用于执行程序启动的时候需要加载的任务. <br>
 * Date: 2016年8月8日 上午11:03:22 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration
@EnableConfigurationProperties(value = { InitQueueProperties.class })
public class InitQueue implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private InitQueueProperties config;
    private boolean isLoad;
    private boolean isExecute;
    static Log log = LogFactory.getLog(InitQueue.class);
    public static Queue<Thread> asyncTaskQueue;
    public static Queue<Thread> syncTaskQueue;
    private String contextName;
    static {
        asyncTaskQueue = new ArrayDeque<>();
        syncTaskQueue = new ArrayDeque<>();
    }

    public static void addSyncTask(Runnable t) {
        syncTaskQueue.add(new Thread(t));
    }

    public static void addAsyncTask(Runnable t) {
        asyncTaskQueue.add(new Thread(t));
    }

    private void loading() {
        if (isLoad)
            return;
        if (config == null) {
            log.info("no search init task config!");
            return;
        }
        contextName=config.getContextName();
        for (String classPath : config.getAsync()) {
            try {
                Runnable runnable = (Runnable) Class.forName(classPath).newInstance();
                asyncTaskQueue.add(new Thread(runnable));
            } catch (Exception e) {
                log.error("load init task queue fail, task class " + classPath + " not found!", e);
            }
        }
        for (String classPath : config.getSync()) {
            try {
                Runnable runnable = (Runnable) Class.forName(classPath).newInstance();
                syncTaskQueue.add(new Thread(runnable));
            } catch (Exception e) {
                log.error("load init task queue fail, task class " + classPath + " not found!", e);
            }
        }
        isLoad = true;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        // "Root WebApplicationContext"
        loading();
        if(isExecute)
            return;
        if (contextName == null) {
            if (arg0.getApplicationContext().getDisplayName().startsWith("Root")) {

                // xml加载
                if (arg0.getApplicationContext().getId().indexOf("XmlWebApplicationContext") != -1) {

                    // 注解加载
                } else {

                }

            } else {// WebApplicationContext for namespace 'springmvc-servlet'
                asyncTaskQueue.forEach(t -> t.start());
                syncTaskQueue.forEach(t -> t.run());
                syncTaskQueue.clear();
                asyncTaskQueue.clear();
                isExecute=true;
            }
        } else {
            if (arg0.getApplicationContext().getDisplayName().contains(contextName)) {
                asyncTaskQueue.forEach(t -> t.start());
                syncTaskQueue.forEach(t -> t.run());
                syncTaskQueue.clear();
                asyncTaskQueue.clear();
                isExecute=true;
            }
        }

    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

}
