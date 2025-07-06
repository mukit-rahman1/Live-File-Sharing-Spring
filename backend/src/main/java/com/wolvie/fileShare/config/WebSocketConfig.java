package com.wolvie.fileShare.config;

import com.wolvie.fileShare.service.CustomerUserDetailsService;
import com.wolvie.fileShare.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.http.server.ServerHttpRequest;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JWTHandshakeInterceptor jwtHandshakeInterceptor;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(handshakeHandler())
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS();
    }

    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request,
                                              WebSocketHandler wsHandler,
                                              Map<String, Object> attributes) {
                String token = (String) attributes.get("token");
                if (token != null && jwtService.isTokenValid(token)) {
                    String username = jwtService.extractUsername(token);
                    System.out.println("[WS] HandshakeHandler Principal: " + username);
                    return () -> username;
                }
                return null; // fallback if no JWT: fail to bind Principal
            }
        };
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");
                    if (token == null) {
                        token = (String) accessor.getSessionAttributes().get("token");
                    }

                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    if (token != null && jwtService.isTokenValid(token)) {
                        String username = jwtService.extractUsername(token);
                        var userDetails = userDetailsService.loadUserByUsername(username);

                        var auth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        accessor.setUser(auth);
                        SecurityContextHolder.getContext().setAuthentication(auth);

                        System.out.println("[WS] ChannelInterceptor Principal: " + username);
                    } else {
                        System.out.println("[WS] ChannelInterceptor: invalid or missing JWT");
                    }
                }

                return message;
            }
        });
    }
}
