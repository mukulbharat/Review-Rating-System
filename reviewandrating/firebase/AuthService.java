package com.krenai.reviewandrating.firebase;

import com.krenai.reviewandrating.constants.ApiResponse;
import com.krenai.reviewandrating.entities.User;
import com.krenai.reviewandrating.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseAuthTokenService firebaseAuthTokenService;

    public ApiResponse<?> loginWithEmail(String email, String password) {
        User user = userRepository.findByEmailAndIsFlagTrue(email)
                .orElse(null);

        if (user == null) {
            return new ApiResponse<>(false, "User not found or deactivated");
        }

        if (!user.getPassword().equals(password)) {
            return new ApiResponse<>(false, "Invalid password");
        }
        try {
            String token = firebaseAuthTokenService.loginToFirebase(email, password);
            return new ApiResponse<>(true, "Login successful", token);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Firebase token generation failed: " + e.getMessage());
        }
    }
}