package com.example.DoroServer.domain.user.repository;

import com.example.DoroServer.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
