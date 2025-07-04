package com.wolvie.fileShare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import com.wolvie.fileShare.config.TransferTracker;
import com.wolvie.fileShare.service.JWTService;

@Controller
public class TransferWebSocketController {

    @Autowired
    private TransferTracker tracker;

    @Autowired
    private JWTService jwtService;

    @MessageMapping("/register-transfer-window")
    public void register(SimpMessageHeaderAccessor accessor) {
        String username = null;

        // Try Principal first
        if (accessor.getUser() != null) {
            username = accessor.getUser().getName();
        } else {
            // Fallback: validate JWT from STOMP headers
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtService.isTokenValid(token)) {
                    username = jwtService.extractUsername(token);
                } else {
                    System.out.println("Invalid JWT in STOMP connectHeaders");
                }
            }
        }

        if (username != null) {
            tracker.add(username);
            System.out.println("Registered transfer window for: " + username);
        } else {
            System.out.println("Registration failed: no valid JWT");
        }
    }
}
