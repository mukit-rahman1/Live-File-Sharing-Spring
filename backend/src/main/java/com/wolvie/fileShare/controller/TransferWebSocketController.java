package com.wolvie.fileShare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import java.security.Principal;

import com.wolvie.fileShare.config.TransferTracker;
import com.wolvie.fileShare.service.JWTService;

@Controller
public class TransferWebSocketController {

    @Autowired
    private TransferTracker tracker;

    @Autowired
    private JWTService jwtService;
    
    @MessageMapping("/register-transfer-window")
    public void register(Principal principal) {
        String username = principal.getName();
        tracker.add(username);
        System.out.println("Registered transfer window for: " + username);
    }
    
}
