package com.example.DoroServer.domain.notification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum NotificationType {
    NOTIFICATION,ANNOUNCEMENT,LECTURE;

    @JsonCreator
    public static NotificationType from(String val){
        for(NotificationType notificationType : NotificationType.values()){
            if(notificationType.name().equals(val)){
                return notificationType;
            }
        }
        return null;
    }}
