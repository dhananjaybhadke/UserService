package com.project.userservice.repository;

import com.project.userservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

//    Optional<Token> findByTokenAnd(String token, boolean deleted);
    Optional<Token> findByTokenAndDeleted(String token, boolean deleted);
}
