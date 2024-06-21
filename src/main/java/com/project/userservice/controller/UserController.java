package com.project.userservice.controller;

import com.project.userservice.dtos.*;
import com.project.userservice.exceptions.InvalidPasswordException;
import com.project.userservice.exceptions.InvalidTokenException;
import com.project.userservice.model.Token;
import com.project.userservice.model.User;
import com.project.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Token logIn(@RequestBody LogInRequestDTO logInRequestDTO) throws InvalidPasswordException {
        Token token =  userService.logIn(logInRequestDTO.getEmail(), logInRequestDTO.getPassword());
//        LogInResponseDTO logInResponseDTO = new LogInResponseDTO();
//        logInResponseDTO.setToken(token.getToken());
        return token;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDTO logOutRequestDTO){
        ResponseEntity<Void> responseEntity = null;
        try {
            userService.logOut(logOutRequestDTO.getToken());
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Something went wrong");
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/validate/{token}")
    public UserDTO validateToken(@PathVariable String token) throws InvalidTokenException {
        User user = userService.validateToken(token);
        return UserDTO.from(user);
    }

    @GetMapping("/{id}")
    public String getUserDetailsById(@PathVariable Long id) {
        System.out.println("Received request for getUserDetails");
        return "User Details having id: " + id;
    }
}

