package com.haojiankang.framework.consumer.utils.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        ServerWebSocketHandler handler = new ServerWebSocketHandler();
        registry.addHandler(handler, "/web/socket/server").addInterceptors(new ServerInterceptor())
                .setAllowedOrigins("*");
        WebSocketHandlerRegistration regis = registry.addHandler(handler, "/web/socket/server/socketjs")
                .addInterceptors(new ServerInterceptor());
        regis.setAllowedOrigins("*");
        regis.withSockJS();

    }

}
