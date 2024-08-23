package org.example.library.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    private final SimpMessagingTemplate messagingTemplate;
//    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();

//    public WebSocketConfig(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*").withSockJS();
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {

        String sessionId = event.getMessage().getHeaders().get("simpSessionId", String.class);
        System.out.println("shashi kumar conneccted");
        try{
            System.out.println(event.getMessage());
            System.out.println(event);
            System.out.println("sesionId");
            System.out.println(sessionId);
        }catch (Exception e){
            e.printStackTrace();
        }
//        if (sessionId != null) {
//            sessionUserMap.put(sessionId, "User-" + sessionId); // Use session ID as a placeholder for user identification
//            broadcastUserStatus(sessionId, true);
//        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getMessage().getHeaders().get("simpSessionId", String.class);
//        System.out.println(sessionId);
//        if (sessionId != null) {
//            sessionUserMap.remove(sessionId);
//            broadcastUserStatus(sessionId, false);
//        }
    }
//
//    private void broadcastUserStatus(String sessionId, boolean isOnline) {
//        String destination = "/topic/online-status";
//        Map<String, Object> statusMessage = new HashMap<>();
//        statusMessage.put("sessionId", sessionId);
//        statusMessage.put("online", isOnline);
//        messagingTemplate.convertAndSend(destination, statusMessage);
//    }
}
