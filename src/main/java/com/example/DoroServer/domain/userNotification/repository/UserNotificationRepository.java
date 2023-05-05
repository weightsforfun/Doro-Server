package com.example.DoroServer.domain.userNotification.repository;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userNotification.entity.UserNotification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    @Query(value="select un from UserNotification un"
            + " join fetch un.user u"
            + " join fetch un.notification n"
            + " where u.id = :id"
    )
    List<UserNotification> findUserNotificationsByUserId(@Param("id") Long userId);
}
