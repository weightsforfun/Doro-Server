package com.example.DoroServer.domain.notification.service;


import com.example.DoroServer.domain.notification.dto.NotificationConfig;
import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.NotificationReq;
import com.example.DoroServer.domain.notification.entity.SubscriptionType;
import com.example.DoroServer.domain.token.service.TokenService;
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
    private final FirebaseMessaging firebaseMessaging;
    private final TokenService tokenService;


    public String sendNotificationToOne(String token, NotificationReq notificationReq){
        Notification noti = Notification.builder()
                .setBody("hey")
                .setTitle("youn's notification")
                .build();
        Message mes = Message.builder()
                .setToken(token)
                .setNotification(noti)
                .build();

        try {
            String response = firebaseMessaging.send(mes);
            return response;
        } catch (FirebaseMessagingException e) {
            log.info(String.valueOf(e));
            throw new BaseException(Code.BAD_REQUEST);
        }

    }

   public void sendAnnouncementNotification(NotificationContentReq notificationContentReq){
       List<String> allTokens = tokenService.findAllTokens()
               .stream()
               .map(tokenDto -> tokenDto.getToken())
               .collect(Collectors.toList());

       AndroidConfig androidConfig = NotificationConfig.androidConfig(notificationContentReq);
       ApnsConfig apnsConfig = NotificationConfig.apnsConfig(notificationContentReq);

       Message mes = Message.builder()
               .setAndroidConfig(androidConfig)
               .setApnsConfig(apnsConfig)
               .setTopic(SubscriptionType.ANNOUNCEMENT.toString())
               .build();
       try {
           firebaseMessaging.send(mes);
       }catch (FirebaseMessagingException e){
           throw new BaseException(Code.NOTIFICATION_PUSH_FAIL);
       }

   }

   public void subscribe(SubscriptionType subscriptionType, String token){
       try {
           firebaseMessaging.subscribeToTopic(List.of("asd"), subscriptionType.toString());
       } catch (FirebaseMessagingException e) {
           throw new BaseException(Code.FORBIDDEN);
       }
   }

    public void unsubscribe(SubscriptionType subscriptionType, String token){
        try {
            firebaseMessaging.unsubscribeFromTopic(List.of("asd"), subscriptionType.toString());
        } catch (FirebaseMessagingException e) {
            throw new BaseException(Code.FORBIDDEN);
        }
    }
}
