package com.example.DoroServer.domain.notification.repository;

import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
