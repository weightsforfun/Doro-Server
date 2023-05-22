package com.example.DoroServer.domain.notification.repository;

import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // NotificationRes 객체 모두를 직접 조회하는 메서드
    @Query("select new com.example.DoroServer.domain.notification.dto.NotificationRes(n.id,n.title,n.body,n.isRead,n.createdAt,n.lastModifiedAt) from Notification n where n.isPublic = :isPublic")
    List<NotificationRes> findAllPublicRes(@Param("isPublic") Boolean isPublic);
}
