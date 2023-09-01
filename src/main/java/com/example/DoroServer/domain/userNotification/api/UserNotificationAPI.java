package com.example.DoroServer.domain.userNotification.api;



import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userNotification.dto.UserNotificationRes;
import com.example.DoroServer.domain.userNotification.service.UserNotificationService;
import com.example.DoroServer.global.common.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "알림📢")
@Slf4j
@RestController
@RequestMapping("/users-notifications")
@RequiredArgsConstructor
public class UserNotificationAPI {

    private final UserNotificationService userNotificationService;

     //모든 Notification 조회 메소드
    @ApiOperation(value = "유저의 전체 알림 조회", notes = "로그인 한 유저의 알람 전체를 불러옵니다. 파라미터로 page랑 size 전달하시면 페이징 됩니다. 이게 Swagger가 잘 안돼서 Postman으로 테스트 해보시는게 나을거에요 Swagger는 이상하게 page랑 size를 인식못하네요")
    @GetMapping()
    public SuccessResponse findUserNotifications(@AuthenticationPrincipal User user,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {

        // 유저별 알림 조희
        List<UserNotificationRes> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId(),
                pageable);

        // 병합 후 조회된 전체 알림 페이징 후반환
        return SuccessResponse.successResponse(userNotifications);
    }

    @ApiOperation(value = "유저의 알림 조회", notes = "userNotificationId(유저별 개인 알람 id)를 전달하면 현재 유저 의 userNotification(개인 알람) 을 전달합니다.")
    @GetMapping("/{notificationId}")
    public SuccessResponse findUserNotification(
            @AuthenticationPrincipal User user,
            @PathVariable("notificationId") Long notificationId) {

        UserNotificationRes userNotificationRes = userNotificationService.findNotificationById(
                user.getId(), notificationId);

        return SuccessResponse.successResponse(userNotificationRes);

    }

    @ApiOperation(value = "유저의 알림 삭제", notes = "userNotificationId 를 전달하면 해당 알림을 삭제합니다.")
    @DeleteMapping("/{userNotificationId}")
    public SuccessResponse deleteUserNotification(
            @PathVariable("userNotificationId") Long userNotificationId) {

        userNotificationService.deleteUserNotification(userNotificationId);

        return SuccessResponse.successResponse(userNotificationId+ "삭제");

    }



}
