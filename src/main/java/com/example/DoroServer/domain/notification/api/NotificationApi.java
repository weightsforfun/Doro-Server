package com.example.DoroServer.domain.notification.api;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.service.NotificationService;
import com.example.DoroServer.domain.user.dto.FindUserRes;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.user.service.UserService;
import com.example.DoroServer.domain.userNotification.service.UserNotificationService;
import com.example.DoroServer.global.common.SuccessResponse;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationApi {

    private final NotificationService notificationService;

    private final UserNotificationService userNotificationService;

    // UserService에 id 로 user 단일 조회 있으면 service로 받아올 수 있는데 없어서 repository 받아옴
    // private final UserService userService;
    private final UserRepository userRepository;

    // 모든 Notification 조회 메소드
    @GetMapping("/{userId}")
    public SuccessResponse findUserNotifications(@PathVariable("userId") Long userId) {
        // 공용 알림만 전체 조회
        List<NotificationRes> notifications = notificationService.findPublicNotifications();

        // 유저별 알림 조희
        List<NotificationRes> userNotifications = notificationService.findUserNotifications(userId);

        // 병합 후 조회된 전체 알림 반환
        notifications. addAll(userNotifications);
        return SuccessResponse.successResponse(notifications);
    }

    // FCM 서버에 알림 전송요청
    @PostMapping
    public SuccessResponse pushNotifications(
            @RequestBody @Valid NotificationContentReq notificationContentReq
            ) {
        if (notificationContentReq.getUserIds() == null || notificationContentReq.getUserIds().isEmpty()) {
            // 유저 ID 리스트가 비어 있으면 모든 사용자에게 알림 전송
            notificationService.sendNotificationToAll(notificationContentReq);

            //공용 알림 저장
            notificationService.saveNotification(notificationContentReq,true);
        } else {
            // 유저 ID 리스트에 있는 사용자에게만 알림 전송
            notificationContentReq.getUserIds().forEach(id ->
            {
                User user = userRepository.findById(id).orElseThrow(() -> {
                            log.info("유저를 찾을 수 없습니다. id = {}", id);
                            throw new BaseException(Code.USER_NOT_FOUND);
                        }
                );
                notificationService.sendMessageToUser(user,notificationContentReq);

                // 개인 알림 저장
                Long notificationId = notificationService.saveNotification(notificationContentReq, false);
                userNotificationService.saveUserNotification(user.getId(), notificationId);
            });
        }
        return SuccessResponse.successResponse("push complete");
    }
}
