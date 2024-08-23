package org.example.library.controllers;

import org.example.library.dto.MessageDto;
import org.example.library.dto.OnlineStatusDto;
import org.example.library.dto.TypingStatusDto;
import org.example.library.service.MessageService;
import org.example.library.service.OnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public MessageDto sendMessage(@Validated @RequestBody MessageDto messageDto) {
        return messageService.sendMessage(messageDto);
    }

    @GetMapping("/chat")
    public List<MessageDto> getMessages(@RequestParam String senderId, @RequestParam String receiverId) {
        return messageService.getMessages(senderId, receiverId);
    }

    @GetMapping("/{id}")
    public MessageDto getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    // Handle sending a WebSocket message
    @MessageMapping("/sendMessage")
    @SendTo("/topic/message")
    public MessageDto sendWebSocketMessage(@Payload MessageDto messageDto) {
        return messageService.sendMessage(messageDto);
    }

    // Handle when a user starts typing
    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public TypingStatusDto handleTyping(@Payload TypingStatusDto typingStatus) {
//        System.out.println(typingStatus);
        return typingStatus;
    }

    // Handle when a user stops typing
    @MessageMapping("/stopTyping")
    @SendTo("/topic/stopTyping")
    public TypingStatusDto handleStopTyping(@Payload TypingStatusDto typingStatus) {
        return typingStatus;
    }

    // Method to broadcast online status
//    @MessageMapping("/updateOnlineStatus")
//    public void updateOnlineStatus(@Payload String userId) {
//        boolean isOnline = onlineUserService.isUserOnline(userId);
//        messagingTemplate.convertAndSend("/topic/online-status", new OnlineStatusDto(userId, isOnline));
//    }
}
