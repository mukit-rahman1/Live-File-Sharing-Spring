package com.wolvie.fileShare.config;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TransferTracker {
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public void add(String username) {
        onlineUsers.add(username);
    }

    public void remove(String username) {
        onlineUsers.remove(username);
    }

    public Set<String> getOnline() {
        return onlineUsers;
    }
}

