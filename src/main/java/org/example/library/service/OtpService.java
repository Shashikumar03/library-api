package org.example.library.service;

import org.example.library.dto.StudentDto;
import org.example.library.payload.JwtResponse;
import org.example.library.payload.OtpResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface OtpService {

  OtpResponse generateOtpPin(String phonenumber);

  ResponseEntity<JwtResponse> verifyOtp(String pin, LocalDateTime time, String email);
}
