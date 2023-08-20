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
    public SuccessResponse sendAll() {

        NotificationContentReq notificationContentReq = NotificationContentReq.builder()
                .body("hi")
                .title("Announcement")
                .notificationType(NotificationType.ANNOUNCEMENT)
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


    // 모든 Notification 조회 메소드
//    @ApiOperation(value = "유저의 전체 알림 조회", notes = "userId를 전달해서 해당 유저의 알림 전체를 조회합니다. 파라미터로 page랑 size 전달하시면 페이징 됩니다. 이게 Swagger가 잘 안돼서 Postman으로 테스트 해보시는게 나을거에요 Swagger는 이상하게 page랑 size를 인식못하네요")
//    @GetMapping("/{userId}")
//    public SuccessResponse findUserNotifications(@PathVariable("userId") Long userId,
//            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
//
//        // 유저별 알림 조희
//        List<NotificationRes> userNotifications = notificationService.findUserNotifications(userId,
//                pageable);
//
//        // 병합 후 조회된 전체 알림 페이징 후반환
//        return SuccessResponse.successResponse(userNotifications);
//    }




//    @ApiOperation(value = "유저의 알림 읽음 처리", notes = "notificationId를 전달해서 해당 알림을 읽음처리 합니다.")
//    @PostMapping("/{notificationId}/doRead")
//    public SuccessResponse findUserNotifications(
//            @PathVariable("notificationId") Long notificationId) {
//
//        notificationService.readNotification(notificationId);
//
//        return SuccessResponse.successResponse("read Notification complete");
//    }



}
