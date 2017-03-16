package com.github.haojiankang.framework.consumer.utils.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ServerWebSocketHandler implements WebSocketHandler {

    public ServerWebSocketHandler() {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        News.del(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        News.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Object msg = message.getPayload();
        if(msg.toString().indexOf("channel")!=-1){
            session.getAttributes().put("channel", msg.toString().replace("channel=", ""));
            News.add(session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        News.del(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    
}
