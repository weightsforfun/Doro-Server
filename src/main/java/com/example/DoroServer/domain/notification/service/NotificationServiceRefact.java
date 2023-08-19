package com.example.DoroServer.domain.notification.service;


import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.notification.entity.NotificationType;
import com.example.DoroServer.domain.notification.entity.SubscriptionType;
import com.example.DoroServer.domain.notification.repository.NotificationRepository;
import com.example.DoroServer.domain.token.entity.Token;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userNotification.service.UserNotificationService;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.google.firebase.messaging.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;
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




    public Long sendNotificationToOne(Long userId, Long targetId,
            NotificationContentReq notificationContentReq) {

        User user = userRepository.findByIdWithTokens(userId)
                .orElseThrow(() -> new BaseException(Code.ACCOUNT_NOT_FOUND));

        List<Token> tokens = user.getTokens();

        AndroidConfig androidConfig = notificationContentReq.toDefaultAndroidConfig();
        ApnsConfig apnsConfig = notificationContentReq.toDefaultApnsConfig();
        NotificationType notificationType = notificationContentReq.getNotificationType();



        try {
            for(Token token: tokens){

                Message mes = Message.builder()
                        .setAndroidConfig(androidConfig)
                        .setApnsConfig(apnsConfig)
                        .setToken(String.valueOf(token))
                        .build();

                firebaseMessaging.send(mes);
            }


            Notification savedNotification = notificationRepository.save(
                    notificationContentReq.toEntity(notificationType, targetId));

            userNotificationService.saveUserNotification(userId,savedNotification.getId());

            return userId;
        } catch (FirebaseMessagingException e) {
            log.info(String.valueOf(e));
            throw new BaseException(Code.BAD_REQUEST);
        }

    }

    public String sendNotificationToAllUsers(NotificationContentReq notificationContentReq,Long targetId) {


        AndroidConfig androidConfig = notificationContentReq.toDefaultAndroidConfig();
        ApnsConfig apnsConfig = notificationContentReq.toDefaultApnsConfig();
        NotificationType notificationType = notificationContentReq.getNotificationType();

        Message mes = Message.builder()
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .setTopic(String.valueOf(notificationType))
                .build();

        try {
            String response = firebaseMessaging.send(mes);

            Notification savedNotification = notificationRepository.save(
                    notificationContentReq.toEntity(notificationType, targetId));

            userNotificationService.SaveAllUserNotification(savedNotification.getId());

            return response;
        } catch (FirebaseMessagingException e) {
            throw new BaseException(Code.NOTIFICATION_PUSH_FAIL);
        }

    }

    public TopicManagementResponse subscribe(SubscriptionType subscriptionType, String token) {
        try {
            TopicManagementResponse response = firebaseMessaging.subscribeToTopic(List.of(token),
                    subscriptionType.toString());
            return response;
        } catch (FirebaseMessagingException e) {
            throw new BaseException(Code.FORBIDDEN);
        }
    }



    public TopicManagementResponse unsubscribe(SubscriptionType subscriptionType,List<Token> tokens) {
        List<String> tokenValues = tokens.stream().map(t -> t.getToken()).collect(Collectors.toList());
        try {
            TopicManagementResponse response = firebaseMessaging.unsubscribeFromTopic(
                    tokenValues, subscriptionType.toString());
            return response;
        } catch (FirebaseMessagingException e) {
            throw new BaseException(Code.FORBIDDEN);
        }
    }
}
