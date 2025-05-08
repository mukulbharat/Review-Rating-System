package com.krenai.reviewandrating;


import com.krenai.reviewandrating.constants.ApiResponse;
import com.krenai.reviewandrating.entities.User;
import com.krenai.reviewandrating.firebase.AuthService;
import com.krenai.reviewandrating.firebase.FirebaseAuthTokenService;
import com.krenai.reviewandrating.otp.OtpService;
import com.krenai.reviewandrating.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseAuthTokenService firebaseAuthTokenService;

    @PostMapping("/email")
    public ApiResponse<?> loginWithEmail(@RequestParam String email, @RequestParam String password) {
        return authService.loginWithEmail(email, password);
    }

    @PostMapping("/phone/request-otp")
    public ApiResponse<?> requestOtp(@RequestParam String phoneNumber) {
        boolean userExists = userRepository.findByPhonenumberAndIsFlagTrue(phoneNumber).isPresent();
        if (!userExists) {
            return new ApiResponse<>(false, "Phone number not registered");
        }

        String otp = otpService.generateOtp(phoneNumber);
        return new ApiResponse<>(true, "OTP sent successfully", otp); // For dev only
    }

    @PostMapping("/phone/verify-otp")
    public ApiResponse<?> verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        boolean valid = otpService.validateOtp(phoneNumber, otp);
//        if (valid) {
//            return new ApiResponse<>(true, "Login successful", phoneNumber);
//        } else {
//            return new ApiResponse<>(false, "Invalid or expired OTP");
//        }

        if(!valid)
            return new ApiResponse<>(false, "Invalid or expired OTP");

        Optional<User> optionalUser = userRepository.findByPhonenumberAndIsFlagTrue(phoneNumber);
        if (optionalUser.isEmpty()) {
            return new ApiResponse<>(false, "User with this phone number not found");
        }

        User user = optionalUser.get();
        try {
            String token = firebaseAuthTokenService.loginToFirebase(user.getEmail(), user.getPassword());
            return new ApiResponse<>(true, "OTP login successful", token);
        } catch (Exception e) {
            return new ApiResponse<>(false, "OTP verified but Firebase token failed: " + e.getMessage());
        }

    }
}
