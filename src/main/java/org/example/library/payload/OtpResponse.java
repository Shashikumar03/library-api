package org.example.library.payload;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.library.dto.StudentDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OtpResponse {

    String otp;
    LocalDateTime localDateTime;
    String message;
    String email;
}
