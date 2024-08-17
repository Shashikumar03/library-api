package org.example.library.controllers;

import org.example.library.dto.MessageDto;
import org.example.library.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    @Autowired
    private MessageService messageService;

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

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageDto sendWebSocketMessage(MessageDto messageDto) {
        return messageService.sendMessage(messageDto);
    }
}