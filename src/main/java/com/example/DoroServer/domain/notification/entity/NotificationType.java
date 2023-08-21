package com.example.DoroServer.domain.notification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum NotificationType {
    NOTIFICATION,ANNOUNCEMENT,LECTURE;

    @JsonCreator
    public static NotificationType fromTestEnum(String val){
        for(NotificationType notificationType : NotificationType.values()){
            System.out.println("notificationType = " + notificationType);
            if(notificationType.name().equals(val)){
                System.out.println("notificationType = " + notificationType);
                return notificationType;
            }
        }
        return null;
    }}
