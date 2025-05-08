package com.krenai.reviewandrating.requestdto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRequestDto {

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotNull(message = "Latitude is required")
    @Min(value = -90, message = "Latitude must be >= -90")
    @Max(value = 90, message = "Latitude must be <= 90")
    private Double latitude;
    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Longitude must be >= -180")
    @Max(value = 180, message = "Longitude must be <= 180")
    private Double longitude;
    private Boolean isFlag;

    @NotNull(message = "tell if user is required")
    private boolean createNewUser;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    //@NotNull(message = "User UUID is required")
    private String userId;

    //@NotNull(message = "Status Code ID is required")
    private Long statusCodeId;

    @Valid
    private UserRequestDto userRequestDto;
}
