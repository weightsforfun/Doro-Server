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
    //todo: sendMessageTo -> sendMessageToAll 함수 만들어서 교체
    @PostMapping
    public SuccessResponse pushNotification(
            @RequestBody NotificationContentReq notificationContentReq) {
        // FCM토큰 가져오기위해 유저 조회
        List<User> users = userRepository.findAll();
        // FCM 서버에 메시지 전송
        // todo: 유저에 토큰 생기면 아래거 이걸로 교체
//        notificationService.sendMessageToAll(notificationContentReq);
        notificationService.sendMessageTo(NotificationReq.builder()
                .targetToken(
                        "fkmvjInlTWOKaXDHUjXOI2:APA91bHhmhodpWb_P9u7i6_tghb4bVTg-L7dli3-tuFfBqT17YcJAy8wK-Eklhf1hPotzT_fR_0KGit4oc8h_b0Hkq56lU_wvLe4GsQ9f5IrOW1rlZ0-82vHaWeLFg0gUpUthYn5PlHS")
                .title(notificationContentReq.getTitle())
                .body(notificationContentReq.getBody())
                .build());

        // 푸쉬알림 저장
        notificationService.saveNotification(notificationContentReq);

        return SuccessResponse.successResponse("push complete");
    }
}
