package com.krenai.reviewandrating.otp;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class OtpService {

    private final Cache otpCache;

    public OtpService(CacheManager cacheManager) {
        this.otpCache = cacheManager.getCache("otpCache");
    }

    public String generateOtp(String phoneNumber) {

        int otp = 100000 + new SecureRandom().nextInt(900000);  // 6-digit OTP
        String otpStr = Integer.toString(otp);
        otpCache.put(phoneNumber, otpStr);
        return otpStr;
    }

    public boolean validateOtp(String phoneNumber, String otp) {
        String cachedOtp = otpCache.get(phoneNumber, String.class);
        return otp.equals(cachedOtp);
    }
}
