package com.example.DoroServer.domain.notification.api;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.SubscriptionDto;
import com.example.DoroServer.domain.notification.service.NotificationServiceRefact;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.global.common.SuccessResponse;
import com.google.firebase.messaging.TopicManagementResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            "알림 제목(title), 내용(body)를 입력받아 알림을 수신 동의한 유저 전체에게 전송합니다.")
    @Secured("ROLE_ADMIN")
    @PostMapping
    public SuccessResponse sendNotificationToAll(
            @RequestBody @Valid NotificationContentReq notificationContentReq) {

        String response = notificationServiceRefact.sendNotificationToAllUsers(
                notificationContentReq, null);

        return SuccessResponse.successResponse(response);
    }

    @ApiOperation(value = "알림 구독", notes = "알림 유형을 선택하고 요청하면 로그인한 유저가 해당 유형의 알림을 받습니다. 로그인시 모든 알림이 구독됩니다.")
    @PostMapping("/subscribe")
    public SuccessResponse subscribe(
            @RequestBody SubscriptionDto subscriptionDto
    ) {
        TopicManagementResponse response = notificationServiceRefact.subscribe(
                subscriptionDto.getNotificationType(),
                subscriptionDto.getFcmToken());

        return SuccessResponse.successResponse(response);
    }

    @ApiOperation(value = "알림 구독 취소", notes = "알림 유형을 선택하고 요청하면 로그인한 유저가 해당 유형의 알림을 받습니다.")
    @PostMapping("/unsubscribe")
    public SuccessResponse unsubscribe(
            @RequestBody SubscriptionDto subscriptionDto
    ) {
        TopicManagementResponse response = notificationServiceRefact.unsubscribe(
                subscriptionDto.getNotificationType(),
                subscriptionDto.getFcmToken());

        return SuccessResponse.successResponse(response);
    }


}
