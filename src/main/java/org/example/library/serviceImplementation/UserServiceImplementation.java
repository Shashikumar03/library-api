package org.example.library.serviceImplementation;

import org.example.library.dto.UserDto;
import org.example.library.entities.User;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.UserRepository;
import org.example.library.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        boolean isPresent= this.userRepository.existsByEmail(userDto.getEmail());
        if(isPresent){
            throw new ApiException("Email already in use");
        }
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(String email) {
        // Find the user by ID and map to UserDto if found
        User user = userRepository.findById(email).orElseThrow(() -> new ResourceNotFoundException("User", email, 0));
         return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        // Find all users and map to UserDto list
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(String email, UserDto userDto) {
        // Find the existing user by ID
        User user = userRepository.findById(email).orElseThrow(() -> new ResourceNotFoundException("User", email, 0));
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        this.userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUser(String email) {
        // Delete the user if exists
        if (userRepository.existsById(email)) {
            userRepository.deleteById(email);
        } else {
            throw new RuntimeException("User not found with ID " + email);
        }
    }
}