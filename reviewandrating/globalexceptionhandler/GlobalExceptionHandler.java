//package com.krenai.reviewandrating.globalexceptionhandler;
//
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(DuplicateUserException.class)
//    public ResponseEntity<ApiResponse<Object>> handleDuplicateUser(DuplicateUserException e) {
//        ApiResponse<Object> response = new ApiResponse<>(
//                false,
//                e.getMessage(),
//                null
//        );
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//    }
//}
