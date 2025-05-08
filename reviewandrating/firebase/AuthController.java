package com.krenai.reviewandrating.firebase;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final FirebaseUserService firebaseUserService;

    public AuthController(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;

    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestParam String uuid,@RequestParam String email, @RequestParam String password) {
        System.out.println("signup controller");
        try {
            System.out.println("Creating User ");
            //String uid = firebaseUserService.createUserWithCustomUid(uuid,email, password);
            return ResponseEntity.ok("User created successfully! UID: " + uuid);
        } catch (Exception e) {
            System.out.println("ëxc");
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // Optional: just to test protected route
//    @GetMapping("/ping")
//    public ResponseEntity<String> ping() {
//        return ResponseEntity.ok("🎯 Auth controller working fine");
//    }
}