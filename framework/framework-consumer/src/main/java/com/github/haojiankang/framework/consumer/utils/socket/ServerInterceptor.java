package com.github.haojiankang.framework.consumer.utils.socket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class ServerInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
            Map<String, Object> map) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            String channel = ((ServletServerHttpRequest) request).getServletRequest().getParameter("channel");
            if (channel != null)
                map.put("channel", channel);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception ex) {
    }
}
