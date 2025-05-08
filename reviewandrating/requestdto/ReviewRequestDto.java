package com.krenai.reviewandrating.requestdto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    @NotNull
    @NotEmpty
    private String reviewText;

    @NotNull
    private Double rating;

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Business ID is required")
    private String businessId;
}

