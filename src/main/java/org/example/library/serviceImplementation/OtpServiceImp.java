package org.example.library.serviceImplementation;

import org.example.library.dto.BookDto;
import org.example.library.dto.StudentDto;
import org.example.library.entities.Student;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.payload.ApiResponse;
import org.example.library.payload.JwtResponse;
import org.example.library.payload.OtpResponse;
import org.example.library.payload.OtpVerify;
import org.example.library.repositories.RoleRepository;
import org.example.library.repositories.StudentRepository;
import org.example.library.security.JwtHelper;
import org.example.library.service.EmailSenderService;
import org.example.library.service.OtpService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OtpServiceImp implements OtpService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JwtHelper helper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    private String otp;
   private LocalDateTime creationTime;
    private Map<String, String> otpMap = new HashMap<>();

    @Override
    public OtpResponse generateOtpPin(String email) {


        boolean studentPresetOrNot = this.studentRepository.existsByEmail(email);
        if(!studentPresetOrNot){
            throw  new ApiException("Invalid email id");
        }

        otp = generateRandomOtp();
        otpMap.put(email,otp);
        emailSenderService.sendSimpleEmail(email,"OTP: "+otp,"OTP for LOGIN"+"\n"+"otp is valid for 5 min only");
        creationTime = LocalDateTime.now();
        return  new OtpResponse(otp,creationTime,"OTP is send Successfully to respective email",email);
    }

    @Override
    public ResponseEntity<JwtResponse> verifyOtp(String pin, LocalDateTime createdTime,String email) {
        String storeOtp = otpMap.get(email);
        if( storeOtp==null){
            throw new ApiException("OTP not generated for this email");
        }
        if(!createdTime.isEqual(creationTime)){
            throw new ApiException("time is Mis-match");
        }
        if(!pin.equals(otp)){
            throw   new ApiException("Invalid OTP");
        }else if(isOtpExpired(createdTime)){
            throw  new ApiException("OTP IS EXPIRED !!");
        }else{
            Student student = this.studentRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user", email, 0));
//            this.doAuthenticate(email, "Shashi@123");

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            System.out.println(userDetails);
            String token = this.helper.generateToken(userDetails);

            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .username(userDetails.getUsername()).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }
//    private void doAuthenticate(String email, String password) {
//
//
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
//        System.out.println(authentication+"authen");
//
//        try {
//            manager.authenticate(authentication);
//
//
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException(" Invalid Username or Password  !!");
//        }
//
//    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> exceptionHandler() {

        ApiResponse apiResponse = new ApiResponse("Invalid Credentials", false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int otpNumber = random.nextInt(900000) + 100000; // Generates a random number between 100000 and 999999
        return String.valueOf(otpNumber);
    }

    private boolean isOtpExpired(LocalDateTime creationTime) {
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();
        long minutesElapsed = creationTime.until(currentTime, java.time.temporal.ChronoUnit.MINUTES);
        System.out.println(minutesElapsed);
        return minutesElapsed >= 5;
    }
}
