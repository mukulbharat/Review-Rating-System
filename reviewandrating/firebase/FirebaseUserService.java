package com.krenai.reviewandrating.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseUserService {

    public void createUserWithCustomUid(String uuid, String email, String password) throws Exception {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setUid(uuid)
                .setEmail(email)
                .setPassword(password);

        FirebaseAuth.getInstance().createUser(request);
        //return userRecord.getUid();
    }
}