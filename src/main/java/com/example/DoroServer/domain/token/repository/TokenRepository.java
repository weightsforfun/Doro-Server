package com.example.DoroServer.domain.token.repository;

import com.example.DoroServer.domain.token.entity.Token;
import com.example.DoroServer.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {


    void deleteAllByUser(User user);

    Optional<Token> findByToken(String token);

    List<Token> findAllByUser(User user);
}
