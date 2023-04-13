package com.example.DoroServer.domain.notification.repository;

import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select new com.example.DoroServer.domain.notification.dto.NotificationRes(n.id,n.title,n.body,n.isRead,n.createdAt,n.lastModifiedAt) from Notification n")
    List<NotificationRes> findAllRes();
}
