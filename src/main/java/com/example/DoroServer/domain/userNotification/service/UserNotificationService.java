package com.example.DoroServer.domain.userNotification.service;

import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.notification.repository.NotificationRepository;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userNotification.dto.UserNotificationRes;
import com.example.DoroServer.domain.userNotification.entity.UserNotification;
import com.example.DoroServer.domain.userNotification.repository.UserNotificationRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    public List<UserNotificationRes> findUserNotificationsByUserId(Long userId, Pageable pageable) {

        List<UserNotification> userNotificationList = userNotificationRepository.findUserNotificationsByUserId(
                userId, pageable);
        List<UserNotificationRes> notificationResList = userNotificationList.stream()
                .map(un -> un.toUserNotificationRes())
                .collect(Collectors.toList());

        return notificationResList;
    }

    @Transactional
    public UserNotificationRes findNotificationById(Long userId,Long notificationId){

        UserNotification userNotification = userNotificationRepository.findUserNotificationByUserIdAndNotificationId(
                userId, notificationId).orElseThrow(() -> new BaseException(Code.FORBIDDEN));

        userNotification.changeIsRead();

        return userNotification.toUserNotificationRes();
    }

    @Transactional
    public Long saveUserNotification(Long userId, Long notificationId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                {
                    log.info("User를 찾을 수 없습니다. id = {}", userId);
                    throw new BaseException(Code.USER_NOT_FOUND);
                }
        );
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        {
                            log.info("Notification을 찾을 수 없습니다. id = {}", notificationId);
                            throw new BaseException(Code.NOTIFICATION_NOT_FOUND);
                        }
                );

        UserNotification userNotification = userNotificationRepository.save(
                UserNotification.builder()
                        .user(user)
                        .notification(notification)
                        .isRead(false)
                        .build());

        return userNotification.getId();
    }

    @Transactional
    public void SaveAllUserNotification(Long notificationId) {
        List<User> userList = userRepository.findAll();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BaseException(Code.ACCOUNT_NOT_FOUND));

        List<UserNotification> userNotifications = userList.stream()
                .filter(user -> user.getIsActive())
                .map(user ->
                     UserNotification.builder()
                            .user(user)
                            .notification(notification)
                            .isRead(false)
                            .build()
                )
                .collect(Collectors.toList());

        userNotificationRepository.saveAll(userNotifications);

    }


    @Transactional
    public void deleteUserNotification(Long id) {
        Optional<UserNotification> userNotification = userNotificationRepository.findById(id);
        userNotification.orElseThrow(() -> {
            log.info("delete 하려는 UserNotification 찾을 수 없습니다. id = {}", id);
            return new BaseException(Code.USER_NOTIFICATION_NOT_FOUND);
        });
        userNotificationRepository.deleteById(id);
    }

}
