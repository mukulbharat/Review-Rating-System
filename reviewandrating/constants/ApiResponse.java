package com.krenai.reviewandrating.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean status;
    private String message;
    private T object;
    private Integer totalItems;

    public ApiResponse(boolean status, String message, T object) {
        this.status = status;
        this.message = message;
        this.object = object;
        this.totalItems = null;
    }

    public ApiResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
        this.object = null;
        this.totalItems = null;
    }


}
