package com.example.DoroServer.global.auth;

import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.Gender;
import com.example.DoroServer.domain.user.entity.StudentStatus;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.global.auth.dto.ChangePasswordReq;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuthTestSetup {

    public static String ACCOUNT = "test12";
    public static String PASSWORD = "password1@";
    public static String PASSWORD_CHECK = "password1@";
    public static String NAME = "name";
    public static LocalDate BIRTH = LocalDate.parse("2023-07-10");
    public static String PHONE = "01011111111";
    public static String SCHOOL = "school";
    public static String STUDENT_ID = "2023045650";
    public static String MAJOR = "major";
    public static int GENERATION = 4;
    public static String NEWPASSWORD = "changepassword1@";
    public static String PROFILE_IMG = "path/to/profile/test.jpg";
    public static String DORO_ADMIN = "DORO_ADMIN";
    public static String DORO_USER = "DORO_USER";
    public static String DORO_ADMIN_CODE = "1111";
    public static String DORO_USER_CODE = "2222";
    public static String EXCEPTION_CODE_FIELD = "code";
    public static String WRONG_PASSWORD = "wrongpassword1@";
    public static String USER_AGENT = "Mozilla/5.0";
    public static String FCM_TOKEN = "fcmTokenRandomValue";
    public static String ACCESS_TOKEN = "accessTokenRandomValue";
    public static String REFRESH_TOKEN = "refreshTokenRandomValue";

    // 개발 코드에도 적용 필요
    public static String USER_AGENT_HEADER = "User-Agent";
    public static String FCM_TOKEN_HEADER = "fcmToken";

    public static ChangePasswordReq setUpChangePasswordReq(String passwordCheck) {
        return ChangePasswordReq.builder()
            .account(ACCOUNT)
            .phone(PHONE)
            .newPassword(NEWPASSWORD)
            .newPasswordCheck(passwordCheck)
            .build();
    }


    public static JoinReq getJoinReq(String passwordCheck, UserRole role, String authNum) {
        return JoinReq.builder()
            .account(ACCOUNT)
            .password(PASSWORD)
            .passwordCheck(passwordCheck)
            .name(NAME)
            .birth(BIRTH)
            .gender(Gender.MALE)
            .phone(PHONE)
            .school(SCHOOL)
            .studentId(STUDENT_ID)
            .major(MAJOR)
            .studentStatus(StudentStatus.ATTENDING)
            .generation(GENERATION)
            .role(role)
            .doroAuth(authNum)
            .notificationAgreement(Boolean.TRUE)
            .build();
    }

    public static User setUpUser(){
        return User.builder()
            .id(1L)
            .account(ACCOUNT)
            .password(PASSWORD)
            .name(NAME)
            .birth(BIRTH)
            .gender(Gender.MALE)
            .phone(PHONE)
            .degree(
                Degree.builder()
                    .school(SCHOOL)
                    .studentId(STUDENT_ID)
                    .major(MAJOR)
                    .studentStatus(StudentStatus.ATTENDING)
                    .build()
            )
            .generation(GENERATION)
            .role(UserRole.ROLE_USER)
            .profileImg(PROFILE_IMG)
            .notificationAgreement(true)
            .isActive(true)
            .build();
    }
}
