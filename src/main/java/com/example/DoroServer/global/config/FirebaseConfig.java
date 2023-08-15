package com.example.DoroServer.global.config;




import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {
    @Value("classpath:firebase/firebase_service_key.json")
    private Resource resource;

    @PostConstruct
    public FirebaseApp initFireBase(){
        try {
            // Service Account를 이용하여 Fireabse Admin SDK 초기화
            FileInputStream serviceAccount = new FileInputStream(resource.getFile());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
            return firebaseApp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Bean
    public FirebaseMessaging firebaseMessaging(){
        FirebaseMessaging instance = FirebaseMessaging.getInstance();
        return instance;
    }

}