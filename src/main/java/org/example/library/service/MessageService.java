package org.example.library.service;


import org.example.library.dto.MessageDto;

import java.util.List;
import java.util.Optional;

public interface MessageService  {
    MessageDto sendMessage(MessageDto messageDto);
    List<MessageDto> getMessages(String senderId, String receiverId);
    MessageDto getMessageById(Long id);
}