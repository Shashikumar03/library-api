package org.example.library.service;

//package org.example.library.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {

    // Set to store online users
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    // Add a user to the online list
    public void userOnline(String userId) {
        onlineUsers.add(userId);
    }

    // Remove a user from the online list
    public void userOffline(String userId) {
        onlineUsers.remove(userId);
    }

    // Check if a specific user is online
    public boolean isUserOnline(String userId) {
        return onlineUsers.contains(userId);
    }

    // Get all online users
    public Set<String> getAllOnlineUsers() {
        return onlineUsers;
    }
}
