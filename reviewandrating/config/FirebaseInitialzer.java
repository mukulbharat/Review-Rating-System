package com.krenai.reviewandrating.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
        Now, create a configuration class that loads that (firebase-config).json
        file and initializes Firebase when your app starts.
 */
@Configuration
public class FirebaseInitialzer {

    @PostConstruct
    public void init() throws IOException {

        try{
        InputStream serviceAccount =
                getClass().getClassLoader().getResourceAsStream("firebase/firebase-config.json");
        System.out.println("jbjhbj");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized");
        }
        }catch (Exception e)
        {
            throw new RuntimeException("❌ Failed to initialize Firebase: " + e.getMessage(), e);
        }

    }
}
