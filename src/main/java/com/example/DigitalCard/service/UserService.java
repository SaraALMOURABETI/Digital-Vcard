package com.example.DigitalCard.service;



import com.example.DigitalCard.dto.UserDto;
import com.example.DigitalCard.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    User findByPassword(String password);

    List<UserDto> findAllUsers();

    User findByEmailAndPassword(String email, String password);

    void loginUser(UserDto userDto);
}
