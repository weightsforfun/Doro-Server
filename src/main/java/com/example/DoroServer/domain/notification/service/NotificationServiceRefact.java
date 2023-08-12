package com.example.DoroServer.domain.notification.service;


import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationServiceRefact {
    public String sendNotificationToOne(){
        Notification noti = Notification.builder()
                .setBody("hey")
                .setTitle("youn's notification")
                .build();
        Message mes = Message.builder()
                .setToken("123")
                .setNotification(noti)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(mes);
            return response;
        } catch (FirebaseMessagingException e) {
            log.info(String.valueOf(e));
            throw new BaseException(Code.BAD_REQUEST);
        }

    }
}
