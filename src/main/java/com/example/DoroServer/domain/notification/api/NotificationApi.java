package com.example.DoroServer.domain.notification.api;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.entity.NotificationType;
import com.example.DoroServer.domain.notification.entity.SubscriptionType;
import com.example.DoroServer.domain.notification.service.NotificationServiceRefact;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.common.SuccessResponse;
import com.google.firebase.messaging.TopicManagementResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "알림📢")
@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationApi {


    private final NotificationServiceRefact notificationServiceRefact;




    @ApiOperation(value = "알림 전체 전송", notes =
            "알림 제목(title), 내용(body)를 입력받아 알림을 생성합니다.")
    @Secured("ROLE_ADMIN")
    @PostMapping
    public SuccessResponse sendNotificationToAll(
            @RequestBody @Valid NotificationContentReq notificationContentReq) {

        String response = notificationServiceRefact.sendNotificationToAllUsers(
                notificationContentReq,null);

        return SuccessResponse.successResponse(response);
    }



    @GetMapping("/sendAll")
    public SuccessResponse sendAll(@RequestBody @Valid NotificationContentReq notificationContentReq) {

       NotificationContentReq.builder()
                .body("hi")
                .title("Announcement")
                .notificationType(notificationContentReq.getNotificationType())
                .build();

        String response = notificationServiceRefact.sendNotificationToAllUsers(
                notificationContentReq,1L);

        return SuccessResponse.successResponse(response);
    }

    @GetMapping("/test")
    public SuccessResponse testAPI() {
        NotificationContentReq notificationContentReq = NotificationContentReq.builder()
                .body("hi")
                .title("my name is")
                .notificationType(NotificationType.ANNOUNCEMENT)
                .build();
        Long userId = notificationServiceRefact.sendNotificationToOne(
                1L,1L,notificationContentReq);
        return SuccessResponse.successResponse(userId);
    }

    @GetMapping("/subscribe")
    public SuccessResponse subscribeTestAPI() {
        TopicManagementResponse response = notificationServiceRefact.subscribe(
                SubscriptionType.ALL,
                "drnbi7uAR4Wh3jFK-W-T8g:APA91bEqBFnq8OU1eOB-2zh2AIWl77Bb5PDuyVuI5YhovSFYUcrakzWa5DvQHF9wlX2M7vPQRo7HHsGaDD0YTgiL7t1tA6XM7LdGqOdeJNYKcdhD4E7JQsV1-Bim2EhQzpi518XQmPpS");
        return SuccessResponse.successResponse(response);
    }

    @GetMapping("/unsubscribe")
    public SuccessResponse unsubscribeTestAPI() {
        TopicManagementResponse response = notificationServiceRefact.unsubscribe(
                SubscriptionType.ALL,
                List.of());
        return SuccessResponse.successResponse(response);
    }




}
