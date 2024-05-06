package com.project.userservice.services;

import com.project.userservice.model.User;
import com.project.userservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signUp(String email, String password, String name) {
        Optional<User> getUserByEmail = userRepository.findByEmail(email);
        if (getUserByEmail.isPresent()) {
            //user is already present in the DB, so no need to signup
            return getUserByEmail.get();
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }
    public void logIn() {}
    public void logOut() {}


}
