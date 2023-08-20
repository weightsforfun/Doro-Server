package com.example.DoroServer.domain.notification.service;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.NotificationDto.Apns;
import com.example.DoroServer.domain.notification.dto.NotificationDto.Aps;
import com.example.DoroServer.domain.notification.dto.NotificationDto.Message;
import com.example.DoroServer.domain.notification.dto.NotificationDto.Payload;
import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.dto.NotificationReq;
import com.example.DoroServer.domain.notification.dto.NotificationDto;
import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.notification.entity.NotificationType;
import com.example.DoroServer.domain.notification.repository.NotificationRepository;
import com.example.DoroServer.domain.token.repository.TokenRepository;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userNotification.entity.UserNotification;
import com.example.DoroServer.domain.userNotification.service.UserNotificationService;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final UserNotificationService userNotificationService;

    private final SaveNotificationService saveNotificationService;

    private final ObjectMapper objectMapper;

    private final String API_URL;       // FCM 전송 Api URL

    private final String PROJECT_ID;    // 생성한 FCM Project Id

    public NotificationService(
            NotificationRepository notificationRepository,
            TokenRepository tokenRepository,
            ObjectMapper objectMapper,
            UserRepository userRepository,
            UserNotificationService userNotificationService,
            SaveNotificationService saveNotificationService,
            @Value("${api.url}") String apiUrl,
            @Value("${project.id}") String projectId) {
        this.notificationRepository = notificationRepository;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userNotificationService = userNotificationService;
        this.saveNotificationService = saveNotificationService;
        this.API_URL = apiUrl;
        this.PROJECT_ID = projectId;
    }

    // 유저 개인의 알림 조회하는 메소드
    public List<NotificationRes> findUserNotifications(Long userId, Pageable pageable) {
        List<UserNotification> userNotifications = userNotificationService
                .findUserNotificationsByUserId(userId, pageable);

        return userNotifications.stream()
                .map(un -> un.getNotification().toRes())
                .collect(Collectors.toList());
    }

    // id에 해당하는 알림을 조회하는 메소드
    public NotificationRes findNotificationById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.orElseThrow(() -> {
            log.info("Notification을 찾을 수 없습니다. id = {}", id);
            throw new BaseException(Code.NOTIFICATION_NOT_FOUND);
        }).toRes();
    }


    // 모든 유저에게 푸쉬알림 발송 후 저장
    @Transactional
    public void sendNotificationToAll(NotificationContentReq notificationContentReq,
                                      NotificationType notificationType, Long announcementId) {
        List<User> users = userRepository.findAllWithTokens();

        Long notificationId = saveNotificationService.saveNotification(
                notificationContentReq, notificationType,announcementId);

        if (!users.isEmpty()) {
            users.stream().forEach(user -> {
                // 유저별로 알림 수신 동의 여부 체크
                userNotificationService.saveUserNotification(user.getId(), notificationId);
                if (user.getNotificationAgreement()) {
                    // 동의했을 경우 보유한 모든 토큰에 알림 발송
                    if(notificationType == NotificationType.ANNOUNCEMENT){
                        user.getTokens().stream().forEach(token -> {
                            NotificationReq notificationReq = NotificationReq.builder()
                                    .targetToken(token.getToken())
                                    .title(notificationContentReq.getTitle())
                                    .body(notificationContentReq.getBody())
                                    .id(announcementId)
                                    .build();
                            sendMessageTo(notificationReq, notificationType, user, notificationId);
                        });
                    } else if(notificationType == NotificationType.NOTIFICATION) {
                        user.getTokens().stream().forEach(token -> {
                            NotificationReq notificationReq = NotificationReq.builder()
                                    .targetToken(token.getToken())
                                    .title(notificationContentReq.getTitle())
                                    .body(notificationContentReq.getBody())
                                    .id(notificationId)
                                    .build();
                            sendMessageTo(notificationReq, notificationType, user, notificationId);
                        });
                    }
                }
            });
        }
    }

    // 선택한 유저에게 알림 전송
    @Transactional
    public void sendNotificationsToSelectedUsers(NotificationContentReq notificationContentReq,
            NotificationType notificationType) {
        notificationContentReq.getUserIds().forEach(id ->
        {
            User user = userRepository.findByIdWithTokens(id).orElseThrow(() -> {
                        log.info("유저를 찾을 수 없습니다. id = {}", id);
                        throw new BaseException(Code.USER_NOT_FOUND);
                    }
            );
            // 알림 저장
            Long notificationId = saveNotificationService.saveNotification(
                    notificationContentReq, NotificationType.NOTIFICATION,null);
            userNotificationService.saveUserNotification(id, notificationId);

            // 유저별로 알림 수신 동의 여부 체크
            if (user.getNotificationAgreement()) {
                // 동의했을 경우 보유한 모든 토큰에 알림 발송
                user.getTokens().stream()
                        .forEach(token -> {
                            NotificationReq notificationReq = NotificationReq.builder()
                                    .targetToken(token.getToken())
                                    .title(notificationContentReq.getTitle())
                                    .body(notificationContentReq.getBody())
                                    .id(notificationId)
                                    .build();
                            //sendMessageTo(notificationReq, notificationType);
                        });
            }
        });
    }

    public void sendFixedMessageToUser(User user, String title, String body) {
        if (user.getNotificationAgreement()) {
            user.getTokens().stream()
                    .forEach(token -> {
                        NotificationReq notificationReq = NotificationReq.builder()
                                .targetToken(token.getToken())
                                .title(title)
                                .body(body)
//                                .id(notificationId)
                                .build();
                        //sendMessageTo(notificationReq,NotificationType.NOTIFICATION);
                    });
        }
    }


    // FCM 메시지를 보내는 메소드
    public void sendMessageTo(NotificationReq notificationReq, NotificationType notificationType,
            User user, Long notificationId) {
        // FCM 메시지 생성
        String message = makeMessage(notificationReq, notificationType);


        OkHttpClient httpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));


            // HTTP 요청 생성
        Request request = null;
        try {
            request = new Request.Builder()
                    .url(API_URL.replace("{project-id}", PROJECT_ID))
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();
        } catch (IOException e) {
            throw new BaseException(Code.NOTIFICATION_PUSH_FAIL);
        }

        // HTTP 요청 비동기 실행
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 응답을 받았을 때 처리 로직
                    int statusCode = response.code();
                    log.info("response 확인 ---------------: {}",response);
                    if (response.isSuccessful()){
                        log.info("알림 전송 성공: {}", user.getId());
                    } else {
                        log.warn("유효하지 않은 FCM 토큰: {}", response.body().string());
                    }
                    response.close();
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    // Retry 또는 에러 처리 구현 필요
                    log.error("FCM 서버 일시적 에러: {}", e.getMessage());
                }
            });

        // 프로그램 종료 시에 httpClient 정리
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            httpClient.dispatcher().executorService().shutdown();
        }));
    }


    // FCM 메시지를 생성하는 메소드
    private String makeMessage(NotificationReq notificationReq, NotificationType notificationType) {
        NotificationDto fcmMessage =
                NotificationDto.builder()
                        .message(
                                Message.builder()
                                        .token(notificationReq.getTargetToken())
                                        .notification(
                                                NotificationDto.Notification.builder()
                                                        .title(notificationReq.getTitle())
                                                        .body(notificationReq.getBody())
                                                        .image(null)
                                                        .build())
                                        .apns(
                                                Apns.builder()
                                                        .payload(
                                                                Payload.builder()
                                                                        .aps(Aps.builder()
                                                                                .sound("default")
                                                                                .build())
                                                                        .build())
                                                        .build())
                                        .build())
                        .validateOnly(false)
                        .build();

        String fcmMessageString = "";

        try {
            fcmMessageString = objectMapper.writeValueAsString(fcmMessage);
//            log.info("{}",fcmMessageString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return fcmMessageString;
    }

    // GoogleApi 사용하기 위해 Firebase 에서부터 AccessToken 발급받는 메소드
    private String getAccessToken() throws IOException {
        // 서비스 키 파일이 위치한 경로
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        // 서비스 키 파일로 인증정보 획득
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(
                                new ClassPathResource(firebaseConfigPath).getInputStream())
                        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        // 토큰 만료 확인
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // 확인한 알림 읽음 처리
//    @Transactional
//    public void readNotification(Long id) {
//        Optional<Notification> findNotification = notificationRepository.findById(id);
//        Notification notification = findNotification.orElseThrow(() -> {
//            log.info("Notification을 찾을 수 없습니다. id = {}", id);
//            throw new BaseException(Code.NOTIFICATION_NOT_FOUND);
//        });
//        notification.isReadTrue();
//    }

}

