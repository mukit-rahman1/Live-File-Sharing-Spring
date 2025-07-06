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
    
        System.out.println("Handshake attempt");
    
        String query = request.getURI().getQuery();
        String token = null;
    
        if (query != null && query.startsWith("token=")) {
            token = query.substring(6);
        }
    
        if (token != null && !token.isEmpty()) {
            attributes.put("token", token);
            System.out.println("Found WS token: " + token);
        } else {
            System.out.println("No WS token found");
        }
    
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
