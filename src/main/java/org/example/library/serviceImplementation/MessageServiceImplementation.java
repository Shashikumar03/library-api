package org.example.library.serviceImplementation;


import org.example.library.dto.MessageDto;
import org.example.library.entities.Message;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.MessageRepository;
import org.example.library.repositories.UserRepository;
import org.example.library.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class MessageServiceImplementation implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MessageDto sendMessage(MessageDto messageDto) {

        String senderEmailId = messageDto.getSenderId();
        String receiverEmailId = messageDto.getReceiverId();
//        if(!this.userRepository.existsByEmail(receiverEmailId))
//            throw  new ApiException(receiverEmailId + " this is not a valid user");
//        if(!this.userRepository.existsByEmail(senderEmailId))
//            throw  new ApiException(senderEmailId + " this is not a valid user");

        messageDto.setTimestamp(LocalDateTime.now());
        Message message = modelMapper.map(messageDto, Message.class);
        Message savedMessage = messageRepository.save(message);
        return modelMapper.map(savedMessage, MessageDto.class);

        // chillaneka code kidhar hai?
    }

    @Override
    public List<MessageDto> getMessages(String senderId, String receiverId) {
        if(!this.userRepository.existsByEmail(senderId))
            throw  new ApiException(senderId + " this is not a valid user");
        if(!this.userRepository.existsByEmail(receiverId))
            throw  new ApiException(receiverId + " this is not a valid user");

        // Retrieve messages sorted by timestamp
//        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampAsc(senderId, receiverId);
        List<Message> messages = messageRepository.findMessagesBetweenUsers(senderId, receiverId);

        // Convert entities to DTOs
        return messages.stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto getMessageById(Long id) {
        // Find message by ID and map to DTO if found
        Message message = messageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("message", "messageId", id));
        return modelMapper.map(message, MessageDto.class);
    }
}