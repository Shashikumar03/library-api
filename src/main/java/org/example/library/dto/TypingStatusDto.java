package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TypingStatusDto {
    private String senderId;
    private String receiverId;
    private boolean isTyping;

    // Getters and Setters
}
