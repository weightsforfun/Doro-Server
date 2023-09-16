package com.example.DoroServer.domain.notification.service;


import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.notification.entity.NotificationType;
import com.example.DoroServer.domain.notification.repository.NotificationRepository;
import com.example.DoroServer.domain.token.entity.Token;
import com.example.DoroServer.domain.token.service.TokenService;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userNotification.service.UserNotificationService;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.exception.FCMException;
import com.google.firebase.messaging.*;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationServiceRefact {


    private final UserNotificationService userNotificationService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final TokenService tokenService;


    public Long sendNotificationToOne(Long userId, Long targetId,
            NotificationContentReq notificationContentReq) {
        User user = userRepository.findByIdWithTokens(userId)
                .orElseThrow(() -> new BaseException(Code.ACCOUNT_NOT_FOUND));

        List<Token> tokens = user.getTokens();

        List<String> tokenString = tokens.stream()
                .map(t -> String.valueOf(t.getToken()))
                .collect(Collectors.toList());

        AndroidConfig androidConfig = notificationContentReq.toDefaultAndroidConfig();
        ApnsConfig apnsConfig = notificationContentReq.toDefaultApnsConfig();
        NotificationType notificationType = notificationContentReq.getNotificationType();

        try {

            MulticastMessage message = MulticastMessage.builder()
                    .setAndroidConfig(androidConfig)
                    .setApnsConfig(apnsConfig)
                    .addAllTokens(tokenString)
                    .build();

            BatchResponse response = firebaseMessaging.sendMulticast(message);
            List<SendResponse> responses = response.getResponses();

            for (int i = 0; i < responses.size(); i++) {
                //유효하지 않은 토큰일 경우 삭제해야한다.
                //FCM docs 에 UNREGISTERED, INVALID_ARGUMENT 일 경우 유효하지 않은 토큰이므로 삭제하라고 권고한다.

                SendResponse sr = responses.get(i);
                //sr 이 Successful 이 아니면 NotNull 을 보장한다.
                if (!sr.isSuccessful()) {

                    MessagingErrorCode messagingErrorCode = sr.getException()
                            .getMessagingErrorCode();

                    if (messagingErrorCode == MessagingErrorCode.UNREGISTERED
                            || messagingErrorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                        log.info(user.getName()+"token 이 만료되어 제거되었습니다.");
                        tokenService.deleteToken(userId, tokenString.get(i));
                    }
                }
            }

            Notification savedNotification = notificationRepository.save(
                    notificationContentReq.toEntity(notificationType, targetId));

            userNotificationService.saveUserNotification(userId, savedNotification.getId());

            return userId;
        } catch (FirebaseMessagingException e) {
            throw new FCMException(e.getMessagingErrorCode(), e.getMessage());
        }

    }

    public String sendNotificationToAllUsers(NotificationContentReq notificationContentReq,
            Long targetId) {

        AndroidConfig androidConfig = notificationContentReq.toDefaultAndroidConfig();
        ApnsConfig apnsConfig = notificationContentReq.toDefaultApnsConfig();
        NotificationType notificationType = notificationContentReq.getNotificationType();

        Message mes = Message.builder()
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .setTopic(String.valueOf(notificationContentReq.getNotificationType()))
                .build();

        try {
            String response = firebaseMessaging.send(mes);

            Notification savedNotification = notificationRepository.save(
                    notificationContentReq.toEntity(notificationType, targetId));

            userNotificationService.SaveAllUserNotification(savedNotification.getId());

            return response;
        } catch (FirebaseMessagingException e) {
            throw new FCMException(e.getMessagingErrorCode(), e.getMessage());
        }

    }

    public TopicManagementResponse subscribe(NotificationType notificationType, String token,Long userId) {
        try {
            TopicManagementResponse response = firebaseMessaging.subscribeToTopic(List.of(token),
                    notificationType.toString());
            return response;
        } catch (FirebaseMessagingException e) {
            MessagingErrorCode messagingErrorCode = e.getMessagingErrorCode();

            if (messagingErrorCode == MessagingErrorCode.UNREGISTERED
                    || messagingErrorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                log.info(userId+" token 이 만료되어 제거되었습니다.");
                tokenService.deleteToken(userId, token);
            }

            throw new FCMException(e.getMessagingErrorCode(), e.getMessage());
        }
    }


    public TopicManagementResponse unsubscribe(NotificationType notificationType,
            String token, Long userId) {
        try {
            TopicManagementResponse response = firebaseMessaging.unsubscribeFromTopic(
                    List.of(token), notificationType.toString());
            return response;
        } catch (FirebaseMessagingException e) {

            MessagingErrorCode messagingErrorCode = e.getMessagingErrorCode();

            if (messagingErrorCode == MessagingErrorCode.UNREGISTERED
                    || messagingErrorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                log.info(userId+" token 이 만료되어 제거되었습니다.");
                tokenService.deleteToken(userId, token);
            }

            throw new FCMException(e.getMessagingErrorCode(), e.getMessage());
        }
    }


}
