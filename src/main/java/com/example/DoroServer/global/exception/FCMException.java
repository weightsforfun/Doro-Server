package com.example.DoroServer.global.exception;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FCMException extends RuntimeException{
    private MessagingErrorCode messagingErrorCode;
    private String message;

}
