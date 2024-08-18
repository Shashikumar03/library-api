package org.example.library.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class    MessageDto {

//    @NotNull(message = "id not be null")
    private Long id;

    @NotNull(message = "senderId not be null")
    private String senderId;
    @NotNull(message = "receiverId not be null")
    private String receiverId;
    @NotEmpty(message = "message should not be empty")
    private String text;
    private LocalDateTime timestamp;
}