package com.example.DoroServer.domain.notification.service;


import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.NotificationReq;
import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.notification.entity.SubscriptionType;
import com.example.DoroServer.domain.notification.repository.NotificationRepository;
import com.example.DoroServer.domain.token.entity.Token;
import com.example.DoroServer.domain.token.service.TokenService;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userNotification.entity.UserNotification;
import com.example.DoroServer.domain.userNotification.repository.UserNotificationRepository;
import com.example.DoroServer.domain.userNotification.service.UserNotificationService;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.google.firebase.messaging.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        SubscriptionType subscriptionType = notificationContentReq.getSubscriptionType();



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
                    notificationContentReq.toEntity(subscriptionType, targetId));

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
        SubscriptionType subscriptionType = notificationContentReq.getSubscriptionType();

        Message mes = Message.builder()
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .setTopic(String.valueOf(subscriptionType))
                .build();

        try {
            String response = firebaseMessaging.send(mes);

            Notification savedNotification = notificationRepository.save(
                    notificationContentReq.toEntity(subscriptionType, targetId));

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

    public TopicManagementResponse unsubscribe(SubscriptionType subscriptionType, String token) {
        try {
            TopicManagementResponse response = firebaseMessaging.unsubscribeFromTopic(
                    List.of(token), subscriptionType.toString());
            return response;
        } catch (FirebaseMessagingException e) {
            throw new BaseException(Code.FORBIDDEN);
        }
    }
}
