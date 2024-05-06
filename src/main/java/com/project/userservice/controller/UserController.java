package com.project.userservice.controller;

import com.project.userservice.dtos.*;
import com.project.userservice.model.User;
import com.project.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        User user =  userService.signUp(
                signUpRequestDTO.getEmail(), signUpRequestDTO.getPassword(), signUpRequestDTO.getName()
        );
        return UserDTO.from(user);
    }

    @PostMapping("/login")
    public LogInResponseDTO logIn(@RequestBody LogInRequestDTO logInRequestDTO){
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDTO logOutRequestDTO){
        return null;
    }
}

