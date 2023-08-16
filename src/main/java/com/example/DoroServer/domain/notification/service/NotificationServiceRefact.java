package com.example.DoroServer.domain.notification.service;


import com.example.DoroServer.domain.notification.dto.NotificationReq;
import com.example.DoroServer.domain.notification.entity.NotificationType;
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

   public void sendAnnouncementNotification(NotificationReq notificationReq){
       List<String> allTokens = tokenService.findAllTokens()
               .stream()
               .map(tokenDto -> tokenDto.getToken())
               .collect(Collectors.toList());

       Notification noti = Notification.builder()
               .setBody("hey")
               .setTitle("youn's notification")
               .build();

       Message mes = Message.builder()
               .setNotification(noti)
               .setTopic(NotificationType.ANNOUNCEMENT.toString())
               .build();
       try {
           firebaseMessaging.send(mes);
       }catch (FirebaseMessagingException e){
           throw new BaseException(Code.NOTIFICATION_PUSH_FAIL);
       }

   }

   public void subscribe(NotificationType notificationType,String token){
       try {
           firebaseMessaging.subscribeToTopic(List.of("asd"),notificationType.toString());
       } catch (FirebaseMessagingException e) {
           throw new BaseException(Code.FORBIDDEN);
       }
   }

    public void unsubscribe(NotificationType notificationType,String token){
        try {
            firebaseMessaging.unsubscribeFromTopic(List.of("asd"),notificationType.toString());
        } catch (FirebaseMessagingException e) {
            throw new BaseException(Code.FORBIDDEN);
        }
    }
}
