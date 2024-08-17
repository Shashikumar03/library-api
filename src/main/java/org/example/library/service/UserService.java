package org.example.library.service;



import org.example.library.dto.UserDto;
import org.example.library.entities.User;

import java.util.List;

public interface UserService {


    UserDto createUser(UserDto user);

    UserDto getUserById(String email);

    List<UserDto> getAllUsers();

    UserDto updateUser(String email, UserDto user);

    void deleteUser(String email);
}