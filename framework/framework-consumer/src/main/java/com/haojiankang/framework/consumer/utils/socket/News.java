/** 
0 * Project Name:basic-core 
 * File Name:News.java 
 * Package Name:com.ghit.web.socket 
 * Date:2016年11月21日上午9:34:02  
*/

package com.haojiankang.framework.consumer.utils.socket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * ClassName:News <br>
 * Function: TODO ADD FUNCTION. <br>
 * Date: 2016年11月21日 上午9:34:02 <br>
 * 
 * @author ren7wei
 * @version
 * @since JDK 1.8
 * @see
 */
public class News {

    protected final static Log LOGGER = LogFactory.getLog(News.class);

    /**
     * 
     * publish:向指定频道发布消息
     * 
     * @author ren7wei
     * @param channel
     * @param message
     * @return
     * @since JDK 1.8
     */
    public static boolean publish(String channel, String message) {
        Set<WebSocketSession> set = sessions.get(channel);
        TextMessage msg = new TextMessage(message);
        if (set != null)
            synchronized (News.class) {
                set.stream().filter(s -> s.isOpen()).forEach(s -> {
                    try {
                        s.sendMessage(msg);
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                });
            }
        return true;
    }

    private final static Map<String, Set<WebSocketSession>> sessions;
    static {
        sessions = new HashMap<>();
    }

    public static void add(WebSocketSession session) {
        Object channel = session.getAttributes().get("channel");
        if (channel == null)
            return;
        synchronized (News.class) {
            Set<WebSocketSession> set = sessions.get(channel);
            if (set == null) {
                set = new HashSet<>();
                sessions.put(channel.toString(), set);
            }
            set.add(session);
        }
    }

    public static void del(WebSocketSession session) {
        Object channel = session.getAttributes().get("channel");
        if (channel == null)
            return;
        synchronized (News.class) {
            Set<WebSocketSession> set = sessions.get(channel);
            if (set != null) {
                set.remove(session);
            }
        }

    }
}
