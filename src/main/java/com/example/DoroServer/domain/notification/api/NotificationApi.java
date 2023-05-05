package com.example.DoroServer.domain.notification.api;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.service.NotificationService;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userNotification.service.UserNotificationService;
import com.example.DoroServer.global.common.SuccessResponse;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "알림📢")
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
    @ApiOperation(value = "유저의 알림 전체 조회", notes = "userId를 전달해서 해당 유저의 알림 전체를 조회합니다.")
    @GetMapping("/{userId}")
    public SuccessResponse findUserNotifications(@PathVariable("userId") Long userId,
            @PageableDefault(page = 0,size = 10,sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        // 공용 알림만 전체 조회
        List<NotificationRes> publicNotifications = notificationService.findPublicNotifications();

        // 유저별 알림 조희
        List<NotificationRes> userNotifications = notificationService.findUserNotifications(userId);

        // 병합 후 조회된 전체 알림 페이징 후반환
        publicNotifications.addAll(userNotifications);
        Collections.sort(publicNotifications, Comparator.comparing(NotificationRes::getCreatedAt, Comparator.reverseOrder()));
            List<NotificationRes> notifications = publicNotifications.stream()
                    .skip((long) pageable.getPageNumber() * pageable.getPageSize()).limit(pageable.getPageSize())
                    .collect(Collectors.toList());
            return SuccessResponse.successResponse(notifications);
    }

    // FCM 서버에 알림 전송요청
    @ApiOperation(value = "알림 전송", notes = "알림 제목(title), 내용(body), 전송할 유저들의 아이디(userIds)를 입력받아 알림을 생성합니다."
            + " userIds를 적지 않거나, 비워두면 사용자 전체에게 전송됩니다.")
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
