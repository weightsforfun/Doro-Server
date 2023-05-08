package com.example.DoroServer.domain.token.repository;

import com.example.DoroServer.domain.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
