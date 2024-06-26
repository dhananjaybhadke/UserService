package com.project.userservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userservice.config.KafkaProducerClient;
import com.project.userservice.dtos.SendEmailBodyDTO;
import com.project.userservice.exceptions.InvalidPasswordException;
import com.project.userservice.exceptions.InvalidTokenException;
import com.project.userservice.model.Token;
import com.project.userservice.model.User;
import com.project.userservice.repository.TokenRepository;
import com.project.userservice.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    TokenRepository tokenRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    KafkaProducerClient kafkaProducerClient;
    ObjectMapper objectMapper;


    UserService(UserRepository userRepository, TokenRepository tokenRepository,BCryptPasswordEncoder bCryptPasswordEncoder, KafkaProducerClient kafkaProducerClient, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
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

        SendEmailBodyDTO sendEmailBodyDTO = new SendEmailBodyDTO();
        sendEmailBodyDTO.setTo(user.getEmail());
        sendEmailBodyDTO.setFrom("dhananjaybhadke10@gmail.com");
        sendEmailBodyDTO.setSubject("Welcome to Scaler");
        sendEmailBodyDTO.setBody("Welcome to Scaler, We are happy to have u in the scaler family");

        try {
            kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(sendEmailBodyDTO));
        } catch (Exception e) {
            System.out.println("Something went wrong while sending a message to Kafka");
        }
        return userRepository.save(user);
    }
    public Token logIn(String email, String password) throws InvalidPasswordException {
        Optional<User> getUserByEmail = userRepository.findByEmail(email);
        if (getUserByEmail.isEmpty()) {
            //user is not present in the DB, so no need to login
            return null;
        }

        User user = getUserByEmail.get();

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            throw new InvalidPasswordException("Invalid Password is provided");
        }

        Token token = generateToken(user);
        return tokenRepository.save(token);

    }
    public void logOut(String token) throws InvalidTokenException {
        Optional<Token> tokenOptional = tokenRepository.findByTokenAndDeleted(token, false);

        if(tokenOptional.isEmpty()){
            throw new InvalidTokenException("Invalid Token");
        }

        Token tokenToBeDeleted = tokenOptional.get();
        tokenToBeDeleted.setDeleted(true);
        tokenRepository.save(tokenToBeDeleted);

    }

    private Token generateToken(User user) {
        LocalDate currentTime = LocalDate.now(); // current time.
        LocalDate thirtyDaysFromCurrentTime = currentTime.plusDays(30);

        Date expiryDate = Date.from(thirtyDaysFromCurrentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryDate(expiryDate);

        //Token value is a randomly generated String of 128 characters.
        token.setToken(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);

        return token;
    }

    public User validateToken(String token) throws InvalidTokenException {
        Optional<Token> tokenOptional = tokenRepository.findByTokenAndDeleted(token, false);

        if(tokenOptional.isEmpty()){
            throw new InvalidTokenException("No entry in token table please pass the correct token");
        }

        Token tokenToBeValidated = tokenOptional.get();
        return tokenToBeValidated.getUser();
    }
}
