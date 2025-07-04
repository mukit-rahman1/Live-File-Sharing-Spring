package com.wolvie.fileShare.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Component
public class JWTHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
        @NonNull ServerHttpRequest request,
        @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler,
        @NonNull Map<String, Object> attributes) {
    
        // Don't reject handshake.
        System.out.println("Handshake attempt");
        return true;
    }
    

    @Override
    public void afterHandshake(
        @NonNull ServerHttpRequest request, 
        @NonNull ServerHttpResponse response, 
        @NonNull WebSocketHandler wsHandler, 
        @Nullable Exception exception) {
    }
    
}
