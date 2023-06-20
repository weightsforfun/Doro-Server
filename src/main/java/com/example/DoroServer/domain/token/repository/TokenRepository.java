package com.example.DoroServer.domain.token.repository;

import com.example.DoroServer.domain.token.entity.Token;
import com.example.DoroServer.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t.user FROM Token t WHERE t.token= :token")
    Optional<User> findUserIdByToken(@Param("token") String token);
}
