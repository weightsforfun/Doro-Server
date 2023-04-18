package com.example.DoroServer.domain.notification.api;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.NotificationReq;
import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.service.NotificationService;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.common.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationApi {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // 모든 Notification 조회 메소드
    @GetMapping
    public SuccessResponse findAllNotifications() {
        List<NotificationRes> notifications = notificationService.findAllNotifications();
        return SuccessResponse.successResponse(notifications);
    }

    // FCM 서버에 알림 전송요청
    @PostMapping
    public SuccessResponse pushNotification(
            @RequestBody NotificationContentReq notificationContentReq) {
        // FCMToken 가져오기위해 유저 조회
        List<User> users = userRepository.findAll();

        // FCM 서버에 메시지 전송
        notificationService.sendMessageToAll(notificationContentReq);

        // 푸쉬알림 저장
        notificationService.saveNotification(notificationContentReq);

        return SuccessResponse.successResponse("push complete");
    }
}
