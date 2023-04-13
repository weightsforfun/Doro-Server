package com.example.DoroServer.domain.notification.service;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.dto.NotificationRes;
import com.example.DoroServer.domain.notification.dto.NotificationReq;
import com.example.DoroServer.domain.notification.dto.NotificationDto;
import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.notification.repository.NotificationRepository;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final String API_URL;

    private final String PROJECT_ID;

    public NotificationService(
            NotificationRepository notificationRepository,
            ObjectMapper objectMapper,
            UserRepository userRepository,
            @Value("${api.url}") String apiUrl,
            @Value("${project.id}") String projectId) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.API_URL = apiUrl;
        this.PROJECT_ID = projectId;
    }

    public List<NotificationRes> findAllNotifications() {
        return notificationRepository.findAllRes();
    }

    public NotificationRes findNotification(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.orElseThrow(() -> {
            log.info("Notification을 찾을 수 없습니다. id = {}", id);
            throw new BaseException(Code.NOTIFICATION_NOT_FOUND);
        }).toRes();
    }

    @Transactional
    public Long createNotification(NotificationContentReq notificationContentReq) {
        Notification notification = notificationContentReq.toEntity();
        notificationRepository.save(notification);
        return notification.getId();
    }

    //todo: user전체 받아와서 user마다 토큰 조회해서 메세지 전송
/*
    public void sendMessageToAll(NotificationContentReq notificationContentReq) {
        List<User> users = userRepository.findAll();
        users.stream().forEach(
                user -> {
                    NotificationReq notificationReq = NotificationReq.builder()
                            .targetToken(user.getTargetToken())
                            .title(notificationContentReq.getTitle())
                            .body(notificationContentReq.getBody())
                            .build();
                    sendMessageTo(notificationReq);
                }
        );
    }
*/


    /**
     * FCM 메시지를 보내는 메소드
     *
     * @param notificationReq
     */
    public void sendMessageTo(NotificationReq notificationReq) {
        // FCM 메시지 생성
        String message = makeMessage(notificationReq);
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));

        try {
        // HTTP 요청 생성
            Request request = new Request.Builder()
                    .url(API_URL.replace("{project-id}", PROJECT_ID))
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();

            // HTTP 요청 실행
            Response response = httpClient.newCall(request).execute();
            if (response != null && response.body() != null) {

                String responseBodyString = response.body().string();
                JsonNode jsonNode = objectMapper.readTree(responseBodyString);

                // 만료된 토큰이거나 잘못된 토큰 체크
                String errorCode = null;
                if (jsonNode.has("error")) {
                    JsonNode detailsNode = jsonNode.get("error").get("details");
                    if (detailsNode.isArray() && detailsNode.size() > 0) {
                        errorCode = detailsNode.get(0).get("errorCode").asText();
                        if (errorCode != null && (errorCode.equals("INVALID_ARGUMENT")
                                || errorCode.equals("UNREGISTERED"))) {
                            // todo: 만료되거나 잘못된 토큰 DB에 있는지 확인하고 삭제
                            log.info("유효하지 않은 토큰 : ");
                            log.info(responseBodyString);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new BaseException(Code.NOTIFICATION_PUSH_FAIL);
        }
    }

    /**
     * FCM 메시지를 생성하는 메소드
     *
     * @param notificationReq
     * @return 생성된 FCM 메시지를 JSON 문자열로 반환
     */
    private String makeMessage(NotificationReq notificationReq) {
        NotificationDto fcmMessage =
                NotificationDto.builder()
                        .message(
                                NotificationDto.Message.builder()
                                        .token(notificationReq.getTargetToken())
                                        .notification(
                                                NotificationDto.Notification.builder()
                                                        .title(notificationReq.getTitle())
                                                        .body(notificationReq.getBody())
                                                        .build())
                                        .apns(
                                                NotificationDto.Apns.builder()
                                                        .payload(
                                                                NotificationDto.Payload.builder()
                                                                        .aps(NotificationDto.Aps.builder()
                                                                                .sound("default")
                                                                                .build())
                                                                        .build())
                                                        .build())
                                        .build())
                        .validateOnly(false)
                        .build();

        String fcmMessageString = "";

        //예외처리 필요
        try {
            fcmMessageString = objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return fcmMessageString;
    }

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

}

