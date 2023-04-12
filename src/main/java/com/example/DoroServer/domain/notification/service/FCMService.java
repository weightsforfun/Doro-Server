package com.example.DoroServer.domain.notification.service;

import com.example.DoroServer.domain.notification.dto.FCMMessageRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/{project-id}/messages:send";
    private final String PROJECT_ID = "test-1c99b";
    private final ObjectMapper objectMapper;
    private final OkHttpClient httpClient;

    public FCMService() {
        objectMapper = new ObjectMapper();
        httpClient = new OkHttpClient.Builder().build();
    }

    /**
     * FCM 메시지를 보내는 메소드
     * @param targetToken FCM 메시지를 받을 대상의 토큰
     * @param title FCM 메시지의 제목
     * @param body FCM 메시지의 내용
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        // FCM 메시지 생성
        String message = makeMessage(targetToken, title, body);
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));

        // HTTP 요청 생성
        Request request = new Request.Builder()
                .url(API_URL.replace("{project-id}", PROJECT_ID))
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        try {
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
            // 예외처리도 수정 필요
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * FCM 메시지를 생성하는 메소드
     * @param targetToken FCM 메시지를 받을 대상의 토큰
     * @param title FCM 메시지의 제목
     * @param body FCM 메시지의 내용
     * @return 생성된 FCM 메시지를 JSON 문자열로 반환
     */
    private String makeMessage(String targetToken, String title, String body) {
        FCMMessageRes fcmMessage =
                FCMMessageRes.builder()
                        .message(
                                FCMMessageRes.Message.builder()
                                        .token(targetToken)
                                        .notification(
                                                FCMMessageRes.Notification.builder()
                                                        .title(title)
                                                        .body(body)
                                                        .build())
                                        .apns(
                                                FCMMessageRes.Apns.builder()
                                                        .payload(
                                                                FCMMessageRes.Payload.builder()
                                                                        .aps(FCMMessageRes.Aps.builder()
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
