package com.example.DoroServer.global.common;

public final class Constants {
    public static final String VERIFIED_CODE = "Verified";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    public static final String REDIS_REFRESH_TOKEN_PREFIX = "RTK";
    public enum REDIS_MESSAGE_PREFIX{
        JOIN, ACCOUNT, PASSWORD, UPDATE
    }
}
